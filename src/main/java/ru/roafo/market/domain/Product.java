package ru.roafo.market.domain;


import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<Price> priceList;

    public Product() {
    }

    public Product(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setPriceList(List<Price> priceList) {
        this.priceList = priceList;
    }

    public List<Price> getPriceList() {
        return priceList;
    }
}