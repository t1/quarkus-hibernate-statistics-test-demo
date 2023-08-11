package com.github.t1;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Getter @Setter @SuperBuilder @NoArgsConstructor
public class Product {
    @Id @GeneratedValue(strategy = SEQUENCE)
    Long id;

    String name;
    String category;
    String description;

    @OneToOne
    Rating rating;
}
