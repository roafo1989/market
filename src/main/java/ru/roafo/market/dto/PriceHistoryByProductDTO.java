package ru.roafo.market.dto;

public class PriceHistoryByProductDTO {
    private String name;
    private Integer frequency;

    public PriceHistoryByProductDTO(String name, Integer frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "\"name\": " + name + ", \"frequency\": " + frequency;
    }
}
