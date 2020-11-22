package ru.roafo.market.dto;

import java.time.LocalDate;

public class PriceHistoryByDateDTO {
    private LocalDate date;
    private Integer frequency;


    public LocalDate getDate() {
        return date;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public PriceHistoryByDateDTO(LocalDate date, Integer frequency) {
        this.date = date;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "\"date\": " + date.toString() +  ", \"frequency\": " + frequency;
    }
}
