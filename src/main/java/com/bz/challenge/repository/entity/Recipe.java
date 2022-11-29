package com.bz.challenge.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
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

    @Column(nullable = false, length = 1000)
    String ingredients;

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
