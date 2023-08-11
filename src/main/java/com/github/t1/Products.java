package com.github.t1;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;

@GraphQLApi
@Transactional
public class Products implements PanacheRepository<Product> {

    @Inject Ratings ratings;

    @Mutation
    public Long createProduct(Product product) {
        if (product.rating != null) ratings.persist(product.rating);
        persist(product);
        return product.getId();
    }

    @Query
    public Product readProduct(Long id) {return findById(id);}

    @Query
    public List<Product> findProductsByCategory(String category) {
        return find("select p from Product p where p.category = ?1", category)
                .list();
    }
}
