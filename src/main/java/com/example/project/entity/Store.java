package com.example.project.entity;

import com.example.project.converter.CapitalizeCharConverter;
import com.example.project.validator.Alphabetic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stores")
public class Store implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id", nullable = false)
    private int id;


    @Column(name = "store_name", nullable = false, unique = true)
    @NotEmpty(message = "Store name should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for store name")
    @Size(min = 3, max = 40, message = "Store name length should be between 3 and 40")
    @Alphabetic(message = "Only alphabetic word are allowed separated optionally by one space for store name")
    @Convert(converter = CapitalizeCharConverter.class)
    private String name;


    @Column(name = "phone", nullable = false, unique = true)
    @NotEmpty(message = "Phone number should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for phone number")
    @Size(min = 8, max = 25, message = "Phone number length should be between 8 and 25")
    @Convert(converter = CapitalizeCharConverter.class)
    private String phone;


    @Column(name = "email", nullable = false, unique = true)
    @NotEmpty(message = "Email should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for email")
    @Email(message = "The given email should be a valid one")
    private String email;


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
    @OneToMany(targetEntity = Staff.class, mappedBy = "store", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ToString.Exclude
    private Set<Staff> staffs;


    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = Stock.class, mappedBy = "store", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ToString.Exclude
    private Set<Stock> stocks;


    @JsonIgnore
    @ToString.Exclude
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = Order.class, mappedBy = "store", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Set<Order> orders;


    public Store(String name, String phone, String email, String address, String city, String state, String zipCode) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

}
