package ru.roafo.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.roafo.market.domain.Price;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PriceHistoryRepo extends JpaRepository<Price, Long> {
    List<Price> findAllByDate(LocalDate date);

    List<Price> findAllByProduct(Long productId);

    @Query("select p.date, count(p.date) from Price p group by p.date")
    List<?> getStatisticByDate();
}
