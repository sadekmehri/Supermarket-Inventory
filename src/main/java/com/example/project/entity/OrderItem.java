package com.example.project.entity;

import com.example.project.converter.PriceConverter;
import com.example.project.key.OrderItemKeys;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "order_item")
@IdClass(OrderItemKeys.class)
public class OrderItem implements Serializable {

    @Id
    @Column(name = "order_item_id", nullable = false)
    private int orderItemId;


    @Id
    @Column(name = "order_id", nullable = false)
    private int orderId;


    @Id
    @Column(name = "product_id", nullable = false)
    private int productId;


    @Column(name = "quantity", nullable = false)
    @Positive(message = "Product quantity should be positive")
    @Min(value = 1, message = "Product quantity should be greater than or equal to 1")
    @Max(value = 1000000, message = "Product quantity should be less than or equal to 1000000")
    private int quantity;


    @Column(name = "price", nullable = false)
    @Positive(message = "Product price should be positive")
    @Min(value = 0, message = "Product price should be greater than or equal to 0")
    @Max(value = 1000000, message = "Product price should be less than or equal to 1000000")
    @Convert(converter = PriceConverter.class)
    private float price;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    private Order order;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    private Product product;

}
