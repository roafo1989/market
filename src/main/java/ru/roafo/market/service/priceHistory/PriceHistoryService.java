package ru.roafo.market.service.priceHistory;

import ru.roafo.market.domain.Price;
import ru.roafo.market.dto.PriceHistoryByDateDTO;
import ru.roafo.market.dto.PriceHistoryByProductDTO;
import ru.roafo.market.dto.PriceHistoryDTO;
import ru.roafo.market.dto.StatisticDTO;

import java.time.LocalDate;
import java.util.List;

public interface PriceHistoryService {
    Price create(Price history);
    List<Price> createList(List<Price> historyList);

    Price update(Price history);

    void delete(Long id);

    Price get(Long id);

    List<Price> getAll();

    List<Price> getByProduct(Long productId);
    List<PriceHistoryDTO> getActualByDate(LocalDate localDate);
    List<PriceHistoryByProductDTO> getStatisticByProduct();
    List<PriceHistoryByDateDTO> getStatisticByDate();
    StatisticDTO getStatistic();

}
