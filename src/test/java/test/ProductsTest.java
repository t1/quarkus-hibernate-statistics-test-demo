package test;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import jakarta.inject.Inject;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.BDDAssertions.then;

@QuarkusTest
@Slf4j
class ProductsTest {

    @GraphQLClientApi
    public interface Api {
        @Mutation Long createProduct(Product product);

        @Query Product readProduct(Long id);

        @Query List<Product> findProductsByCategory(String category);
    }

    @Data @With @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Product {
        Long id;
        String name;
        String description;
        String category;
        Rating rating;
    }

    @Data @With @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Rating {
        Long id;
        @NonNull Integer stars;
    }

    @Inject Api api;
    @Inject SessionFactory sessionFactory;

    @Test
    void shouldCreateProduct() {
        statistics().clear();
        var givenProduct = someProduct();

        var id = api.createProduct(givenProduct);

        log.info("created product {} with id {}", givenProduct, id);
        then(api.readProduct(id)).usingRecursiveComparison().ignoringFields("id", "rating.id").isEqualTo(givenProduct);
        then(statistics().getEntityLoadCount()).isEqualTo(2);
    }

    @Test
    void shouldFindProductsByCategory() {
        var category = "#" + someNumber();
        var product1 = someProduct().withCategory(category);
        var product2 = someProduct().withCategory(category);
        var product3 = someProduct().withCategory(category);
        api.createProduct(product1);
        api.createProduct(product2);
        api.createProduct(product3);
        log.info("prepared 3 products with category {}", category);
        statistics().clear();

        var found = api.findProductsByCategory(category);

        then(found).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "rating.id")
                .containsExactlyInAnyOrder(product1, product2, product3);
        then(statistics().getEntityLoadCount()).isEqualTo(6);
        then(statistics().getQueryExecutionCount()).isOne();
        then(statistics().getEntityFetchCount()).isOne();
    }

    private static Product someProduct() {
        return Product.builder()
                .name("Table-" + someNumber())
                .description("One leg")
                .rating(Rating.builder().stars(5).build())
                .build();
    }

    private static int someNumber() {
        return RANDOM.nextInt(1000, 10000);
    }

    private Statistics statistics() {
        return sessionFactory.getStatistics();
    }

    private static final Random RANDOM = new Random();
}
