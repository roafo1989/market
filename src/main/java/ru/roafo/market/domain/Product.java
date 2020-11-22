package ru.roafo.market.domain;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

@Entity
public class Product {

    @Id
    private Long id;
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<Price> priceList;

    public Product() {
    }

    public Product(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Product(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getId().equals(product.getId()) &&
                getName().equals(product.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}