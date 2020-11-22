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
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    public Float getPrice() {
        return price;
    }


    public LocalDate getDate() {
        return date;
    }

    public Product getProduct() {
        return product;
    }


    private Float price;
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price)) {
            return false;
        }
        return id != null && id.equals(((Price) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
