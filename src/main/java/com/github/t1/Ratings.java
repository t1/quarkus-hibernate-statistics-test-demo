package com.github.t1;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.Dependent;

@Dependent
public class Ratings implements PanacheRepository<Rating> {
}
