package ru.roafo.market.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.roafo.market.domain.Product;
import ru.roafo.market.repository.ProductRepo;

import javax.validation.constraints.NotNull;
import java.util.List;
@Service
@Transactional
public class ProductServiceImplement implements ProductService {

    private final ProductRepo productRepo;

    @Autowired
    public ProductServiceImplement(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Product create(@NotNull Product product) {
        return productRepo.save(product);
    }

    @Override
    public List<Product> createList(@NotNull List<Product> products) {
        return productRepo.saveAll(products);
    }

    @Override
    public Product update(@NotNull Product product) {
        return productRepo.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepo.deleteById(id);
    }

    @Override
    public Product get(Long id) {
        return productRepo.getOne(id);
    }

    @Override
    public List<Product> getAll() {
        return productRepo.findAll();
    }

    @Override
    public Integer getQty() {
        return getAll().size();
    }
}
