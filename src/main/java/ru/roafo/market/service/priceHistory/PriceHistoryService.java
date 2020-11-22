package ru.roafo.market.service.priceHistory;

import ru.roafo.market.dto.PriceHistoryByDateDTO;
import ru.roafo.market.dto.PriceHistoryByProductDTO;
import ru.roafo.market.dto.PriceHistoryDTO;
import ru.roafo.market.dto.StatisticDTO;

import java.time.LocalDate;
import java.util.List;

public interface PriceHistoryService {

    List<PriceHistoryDTO> getActualByDate(LocalDate localDate);

    List<PriceHistoryByProductDTO> getStatisticByProduct();

    List<PriceHistoryByDateDTO> getStatisticByDate();

    StatisticDTO getStatistic();
}
