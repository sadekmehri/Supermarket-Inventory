package com.example.project.entity;

import com.example.project.converter.CapitalizeCharConverter;
import com.example.project.converter.PriceConverter;
import com.example.project.exception.ApiRequestException;
import com.example.project.validator.Alphabetic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private int id;


    @Column(name = "product_qr_code", nullable = false, unique = true)
    @NotEmpty(message = "Product qrcode should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for product qrcode")
    @Size(min = 3, max = 40, message = "Product qrcode length should be between 3 and 40")
    @Convert(converter = CapitalizeCharConverter.class)
    private String qrCode;


    @Column(name = "product_name", nullable = false, unique = true)
    @NotEmpty(message = "Product name should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for product name")
    @Size(min = 3, max = 25, message = "Product name length should be between 3 and 25")
    @Alphabetic(message = "Only alphabetic word are allowed separated optionally by one space for product name")
    @Convert(converter = CapitalizeCharConverter.class)
    private String name;


    @Column(name = "product_picture", nullable = false)
    private String pictureUrl;


    @Column(name = "product_price", nullable = false)
    @Positive(message = "Product price should be positive")
    @Min(value = 0, message = "Product price should be greater than or equal to 0")
    @Max(value = 1000000, message = "Product price should be less than or equal to 1000000")
    @Convert(converter = PriceConverter.class)
    private float price;


    @Transient
    @JsonIgnore
    private MultipartFile file;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id", nullable = false)
    @ToString.Exclude
    private Brand brand;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private Category category;


    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ToString.Exclude
    private Set<Supplier> suppliers;


    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = Stock.class, mappedBy = "product", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ToString.Exclude
    private Set<Stock> stocks;


    @JsonIgnore
    @ToString.Exclude
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = OrderItem.class, mappedBy = "product", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Set<OrderItem> orderItems;


    public Product(String qrCode, String name, String pictureUrl, float price, Brand brand, Category category) {
        this.qrCode = qrCode;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.price = price;
        this.brand = brand;
        this.category = category;
    }

    public void addSupplier(Supplier supplier) {
        findSupplier(supplier);
        this.suppliers.add(supplier);
        supplier.getProducts().add(this);
    }

    public void removeSupplier(int supplierId) {
        Supplier supplier = this.suppliers.stream().filter(s -> s.getId() == supplierId).findFirst().orElse(null);
        if (supplier == null)
            throw new ApiRequestException("Supplier with id " + supplierId + " does not have a relationship with product " + id, HttpStatus.NOT_FOUND);

        this.suppliers.remove(supplier);
        supplier.getProducts().remove(this);
    }

    private void findSupplier(Supplier sp) {
        Supplier supplier = this.suppliers
                .stream()
                .filter(s -> Objects.equals(sp.getName(), s.getName()) && Objects.equals(sp.getPhone(), s.getPhone()) && Objects.equals(sp.getFax(), s.getFax()))
                .findFirst()
                .orElse(null);

        if (supplier != null)
            throw new ApiRequestException("Supplier with id " + supplier.getId() + " have already a relationship with product " + id, HttpStatus.BAD_REQUEST);
    }


}
