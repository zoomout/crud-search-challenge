package com.bz.challenge.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
public class Ingredient {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "ingredients")
    Set<Recipe> recipes;

    @JsonIgnore
    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @Column
    @UpdateTimestamp
    LocalDateTime updatedAt;

}
