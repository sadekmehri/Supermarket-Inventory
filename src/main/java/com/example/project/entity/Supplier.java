package com.example.project.entity;

import com.example.project.converter.CapitalizeCharConverter;
import com.example.project.exception.ApiRequestException;
import com.example.project.validator.Alphabetic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "suppliers")
public class Supplier implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id", nullable = false)
    private int id;


    @Column(name = "company_name", nullable = false, unique = true)
    @NotEmpty(message = "Company name should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for company name")
    @Size(min = 3, max = 25, message = "Company name length should be between 3 and 25")
    @Alphabetic(message = "Only alphabetic word are allowed separated optionally by one space for company name")
    @Convert(converter = CapitalizeCharConverter.class)
    private String name;


    @Column(name = "phone", nullable = false, unique = true)
    @NotEmpty(message = "Phone number should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for phone number")
    @Size(min = 8, max = 25, message = "Phone number length should be between 8 and 25")
    @Convert(converter = CapitalizeCharConverter.class)
    private String phone;


    @Column(name = "fax", nullable = false, unique = true)
    @NotEmpty(message = "Fax number should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for fax number")
    @Size(min = 8, max = 25, message = "Fax number length should be between 8 and 25")
    @Convert(converter = CapitalizeCharConverter.class)
    private String fax;


    @Column(name = "address", nullable = false)
    @NotEmpty(message = "Address should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for address")
    @Size(min = 10, max = 100, message = "Address number length should be between 10 and 100")
    @Convert(converter = CapitalizeCharConverter.class)
    private String address;


    @Column(name = "city", nullable = false)
    @NotEmpty(message = "City name should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for city name")
    @Size(min = 3, max = 25, message = "City name length should be between 3 and 25")
    @Alphabetic(message = "Only alphabetic word are allowed separated optionally by one space for city name")
    @Convert(converter = CapitalizeCharConverter.class)
    private String city;


    @Column(name = "state", nullable = false)
    @NotEmpty(message = "State name should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for state name")
    @Size(min = 3, max = 25, message = "State name length should be between 3 and 25")
    @Alphabetic(message = "Only alphabetic word are allowed separated optionally by one space for state name")
    @Convert(converter = CapitalizeCharConverter.class)
    private String state;


    @Column(name = "zip_code", nullable = false)
    @NotEmpty(message = "Zip code should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for zip code")
    @Size(min = 3, max = 15, message = "Zip code length should be between 3 and 15")
    @Convert(converter = CapitalizeCharConverter.class)
    private String zipCode;


    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_supplier",
            joinColumns = {@JoinColumn(name = "supplier_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "product_id", nullable = false, updatable = false)})
    @ToString.Exclude
    private Set<Product> products = new HashSet<>();


    public Supplier(String name, String phone, String fax, String address, String city, String state, String zipCode) {
        this.name = name;
        this.phone = phone;
        this.fax = fax;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public void addProduct(Product product) {
        this.products.add(product);
        product.getSuppliers().add(this);
    }

    public void removeProduct(int productId) {
        Product product = this.products.stream().filter(p -> p.getId() == productId).findFirst().orElse(null);
        if (product == null)
            throw new ApiRequestException("Product with id " + productId + " does not have a relationship with supplier " + id, HttpStatus.NOT_FOUND);

        this.products.remove(product);
        product.getSuppliers().remove(this);
    }

}
