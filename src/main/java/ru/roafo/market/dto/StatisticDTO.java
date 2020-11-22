package ru.roafo.market.dto;

import java.util.List;

public class StatisticDTO {
    private Integer productQty;
    private List<PriceHistoryByDateDTO> statisticByDate;
    private List<PriceHistoryByProductDTO> statisticByProduct;

    public StatisticDTO(Integer productQty, List<PriceHistoryByDateDTO> statisticByDate, List<PriceHistoryByProductDTO> statisticByProduct) {
        this.productQty = productQty;
        this.statisticByDate = statisticByDate;
        this.statisticByProduct = statisticByProduct;
    }

    public StatisticDTO() {
    }

    @Override
    public String toString() {
        return "StatisticDTO{" +
                "productQty=" + productQty +
                ", statisticByDate=" + statisticByDate +
                ", statisticByProduct=" + statisticByProduct +
                '}';
    }
}
