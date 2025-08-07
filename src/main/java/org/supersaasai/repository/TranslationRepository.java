package org.supersaasai.repository;


import org.supersaasai.entities.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    List<Translation> findByKeyContainingIgnoreCase(String key);
    List<Translation> findByTagsContaining(String tag);
    List<Translation> findByValueContainingIgnoreCase(String value);
    List<Translation> findByLocale(String locale);
    boolean existsByKeyAndLocale(String key, String locale);

}
