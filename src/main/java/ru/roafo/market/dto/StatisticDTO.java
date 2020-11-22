package ru.roafo.market.dto;

import java.util.List;

public class StatisticDTO {
    private Integer productQty;
    private List<PriceHistoryByDateDTO> statisticByDate;
    private List<PriceHistoryByProductDTO> statisticByProduct;

    public StatisticDTO(Integer productQty,
                        List<PriceHistoryByDateDTO> statisticByDate,
                        List<PriceHistoryByProductDTO> statisticByProduct) {
        this.productQty = productQty;
        this.statisticByDate = statisticByDate;
        this.statisticByProduct = statisticByProduct;
    }

    public StatisticDTO() {
    }

    public Integer getProductQty() {
        return productQty;
    }

    public List<PriceHistoryByDateDTO> getStatisticByDate() {
        return statisticByDate;
    }

    public List<PriceHistoryByProductDTO> getStatisticByProduct() {
        return statisticByProduct;
    }

    @Override
    public String toString() {
        return "{" +
                "productQty=" + productQty +
                ", statisticByProduct=" + statisticByProduct +
                ", statisticByDate=" + statisticByDate +
                '}';
    }
}
