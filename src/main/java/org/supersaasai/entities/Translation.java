package org.supersaasai.entities;

import jakarta.persistence.*;
import lombok.*;


import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "translation_table", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"key", "locale"})
})
public class Translation {

    @ElementCollection
    @CollectionTable(
            name = "tags_table", // ✅ Correct table name
            joinColumns = @JoinColumn(name = "translation_id")
    )
    @Column(name = "tag")
    private Set<@NotBlank(message = "Tag must not be blank") String> tags;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Key must not be blank")
    private String key;

    @NotBlank(message = "Value must not be blank")
    @Size(max = 5000, message = "Value must be less than 5000 characters")
    private String value;

    @NotBlank(message = "Locale must not be blank")
    private String locale;

    private Long createdAt;
    private Long updatedAt;

    // Getters/Setters (if needed, or rely on Lombok)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}

