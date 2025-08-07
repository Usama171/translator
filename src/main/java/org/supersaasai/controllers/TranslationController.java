package org.supersaasai.controllers;



import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.supersaasai.entities.Translation;
import org.supersaasai.services.TranslationService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/translations")
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;
    private final ObjectMapper objectMapper;


    @GetMapping("getall")
    public ResponseEntity<List<Translation>> getAll() {
        return ResponseEntity.ok(translationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Translation> getById(@PathVariable Long id) {
        return ResponseEntity.ok(translationService.getById(id));
    }

    @GetMapping("/search/key")
    public ResponseEntity<List<Translation>> searchByKey(@RequestParam String key) {
        return ResponseEntity.ok(translationService.searchByKey(key));
    }

    @GetMapping("/search/tag")
    public ResponseEntity<List<Translation>> searchByTag(@RequestParam String tag) {
        return ResponseEntity.ok(translationService.searchByTag(tag));
    }

    @GetMapping("/search/value")
    public ResponseEntity<List<Translation>> searchByValue(@RequestParam String value) {
        return ResponseEntity.ok(translationService.searchByValue(value));
    }

    @GetMapping("/locale/{locale}")
    public ResponseEntity<List<Translation>> getByLocale(@PathVariable String locale) {
        return ResponseEntity.ok(translationService.getByLocale(locale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        translationService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/create")
    public ResponseEntity<Translation> create(@Valid @RequestBody Translation translation) {
        return ResponseEntity.ok(translationService.create(translation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Translation> update(@PathVariable Long id, @Valid @RequestBody Translation translation) {
        return ResponseEntity.ok(translationService.update(id, translation));
    }
    @PostMapping("/bulk-upload")
    public ResponseEntity<String> bulkUpload(@RequestBody List<Translation> translations) {
        translationService.bulkInsert(translations);
        return ResponseEntity.ok("Bulk upload successful");
    }

    @GetMapping(value = "/export-stream", produces = MediaType.APPLICATION_JSON_VALUE)
    public void exportStream(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=translations.json");

        List<Translation> translations = translationService.getAllTranslations();
        ObjectMapper mapper = new ObjectMapper();
        try (ServletOutputStream out = response.getOutputStream()) {
            mapper.writeValue(out, translations); // streams directly
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadBulkTranslations(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             JsonParser parser = objectMapper.getFactory().createParser(inputStream)) {

            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalArgumentException("Expected JSON array");
            }

            List<Translation> batch = new ArrayList<>();
            while (parser.nextToken() == JsonToken.START_OBJECT) {
                Translation translation = objectMapper.readValue(parser, Translation.class);
                batch.add(translation);

                if (batch.size() >= 1000) {
                    translationService.bulkInsert(batch);
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                translationService.bulkInsert(batch);
            }

            return ResponseEntity.ok("✅ Uploaded in chunks successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed: " + e.getMessage());
        }
    }
}
