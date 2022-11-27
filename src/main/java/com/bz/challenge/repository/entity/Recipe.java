package com.bz.challenge.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.rest.core.annotation.RestResource;

@Entity
@Data
@RestResource
public class Recipe {

    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    Boolean vegetarian;

    @ElementCollection
    @NotEmpty
    @Column(nullable = false)
    Set<String> ingredients;

    @Column(nullable = false, length = 1000)
    String instructions;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    LocalDateTime updatedAt;

}


