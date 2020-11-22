package ru.roafo.market.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;


@Entity
public class Price {
    @Id
    private Long id;
    private Float price;
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    public Price() {
    }

    public Price(Long id, Float price, LocalDate date, Product product) {
        this.id = id;
        this.price = price;
        this.date = date;
        this.product = product;
    }

    public Price(Float price, LocalDate date, Product product) {
        this.price = price;
        this.date = date;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;
        Price price1 = (Price) o;
        return getId().equals(price1.getId()) &&
                getPrice().equals(price1.getPrice()) &&
                getDate().equals(price1.getDate()) &&
                getProduct().equals(price1.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPrice(), getDate(), getProduct());
    }
}
