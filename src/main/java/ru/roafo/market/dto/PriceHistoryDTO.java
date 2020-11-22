package ru.roafo.market.dto;

import ru.roafo.market.domain.Price;

public class PriceHistoryDTO {
    private String name;
    private Float price;

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public PriceHistoryDTO(Price price) {
        this.name = price.getProduct().getName();
        this.price = price.getPrice();
    }
}
