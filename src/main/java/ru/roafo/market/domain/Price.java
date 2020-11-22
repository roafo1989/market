package ru.roafo.market.domain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;


@Entity
public class Price {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Float price;
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    public Price() {
    }

    public Price(Long id, Float price, LocalDate date, Product product){
        this.id = id;
        this.price = price;
        this.date = date;
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public Float getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setProduct(Product product) {
        this.product = product;
    }




}
