package org.supersaasai.services;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supersaasai.entities.Translation;
import org.supersaasai.repository.TranslationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Translation create(Translation translation) {
        boolean exists = translationRepository.existsByKeyAndLocale(translation.getKey(), translation.getLocale());
        if (exists) {
            throw new RuntimeException("Translation with same key and locale already exists");
        }
        translation.setCreatedAt(System.currentTimeMillis());
        translation.setUpdatedAt(System.currentTimeMillis());
        return translationRepository.save(translation);
    }

    public Translation update(Long id, Translation updated) {
        Optional<Translation> optional = translationRepository.findById(id);
        if (optional.isPresent()) {
            Translation existing = optional.get();
            existing.setKey(updated.getKey());
            existing.setValue(updated.getValue());
            existing.setLocale(updated.getLocale());
            existing.setTags(updated.getTags());
            existing.setUpdatedAt(System.currentTimeMillis());
            return translationRepository.save(existing);
        } else {
            throw new RuntimeException("Translation not found with ID: " + id);
        }
    }
    @Transactional(readOnly = true)
    public List<Translation> getAll() {
        return translationRepository.findAll();
    }

    public Translation getById(Long id) {
        return translationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Translation not found"));
    }

    public List<Translation> searchByKey(String key) {
        return translationRepository.findByKeyContainingIgnoreCase(key);
    }

    public List<Translation> searchByTag(String tag) {
        return translationRepository.findByTagsContaining(tag);
    }

    public List<Translation> searchByValue(String value) {
        return translationRepository.findByValueContainingIgnoreCase(value);
    }

    public List<Translation> getByLocale(String locale) {
        return translationRepository.findByLocale(locale);
    }

    public void delete(Long id) {
        translationRepository.deleteById(id);
    }
    @Transactional
    public List<Translation> bulkCreate(List<Translation> translations) {
        long now = System.currentTimeMillis();

        for (Translation t : translations) {
            // Optional: Prevent duplicates
            if (!translationRepository.existsByKeyAndLocale(t.getKey(), t.getLocale())) {
                t.setCreatedAt(now);
                t.setUpdatedAt(now);
            } else {
                throw new RuntimeException("Translation already exists for key: " + t.getKey() + " and locale: " + t.getLocale());
            }
        }

        return translationRepository.saveAll(translations);
    }
    public List<Translation> getAllTranslations() {
        return translationRepository.findAll();
    }
    @Transactional
    public void bulkInsert(List<Translation> translations) {
        translationRepository.saveAll(translations);
    }

}
