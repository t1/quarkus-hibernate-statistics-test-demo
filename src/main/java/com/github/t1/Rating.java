package com.github.t1;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Getter @Setter @SuperBuilder @NoArgsConstructor
public class Rating {
    @Id @GeneratedValue(strategy = SEQUENCE)
    Long id;

    int stars;
}
