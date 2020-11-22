package ru.roafo.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.roafo.market.domain.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
}
