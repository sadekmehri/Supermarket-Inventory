package com.example.project.entity;

import com.example.project.key.StockKeys;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "stocks")
@IdClass(StockKeys.class)
public class Stock {

    @Id
    @Column(name = "product_id", nullable = false)
    private int productId;


    @Id
    @JsonIgnore
    @Column(name = "store_id", nullable = false)
    private int storeId;


    @Column(name = "quantity", nullable = false)
    @Positive(message = "Product quantity should be positive")
    @Min(value = 0, message = "Product quantity should be greater than or equal to 0")
    @Max(value = 1000000, message = "Product quantity should be less than or equal to 1000000")
    private int quantity;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    private Product product;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    private Store store;


    public Stock(int productId, int storeId, int quantity) {
        this.productId = productId;
        this.storeId = storeId;
        this.quantity = quantity;
    }

}
