package com.bz.challenge.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@RestResource(rel = "recipes", path = "recipes")
public class Recipe {

    @JsonIgnore
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Boolean vegetarian;

    @Column(nullable = false)
    Integer servingsNumber;

    @ElementCollection
    @NotEmpty
    @Column(nullable = false)
    Set<String> ingredients;

    @Column(nullable = false, length = 1000)
    String instructions;

    @JsonIgnore
    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @Column
    @UpdateTimestamp
    LocalDateTime updatedAt;

}


