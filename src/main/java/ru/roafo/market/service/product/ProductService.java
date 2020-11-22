package ru.roafo.market.service.product;

import ru.roafo.market.domain.Product;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ProductService {
    Product create(@NotNull Product product);
    List<Product> createList(@NotNull  List<Product> products);

    Product update(@NotNull Product product);

    void delete(Long id);

    Product get(Long id);

    List<Product> getAll();

    Integer getQty();
}
