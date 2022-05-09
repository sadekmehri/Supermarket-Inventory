package com.example.project.entity;

import com.example.project.converter.CapitalizeCharConverter;
import com.example.project.validator.Alphabetic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
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
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private int id;


    @Column(name = "category_name", nullable = false, unique = true)
    @NotEmpty(message = "Brand name should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for brand name")
    @Size(min = 3, max = 25, message = "Brand name length should be between 3 and 25")
    @Alphabetic(message = "Only alphabetic word are allowed separated optionally by one space for category name")
    @Convert(converter = CapitalizeCharConverter.class)
    private String name;


    @Transient
    @JsonIgnore
    @OneToMany(targetEntity = Product.class, mappedBy = "category", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ToString.Exclude
    private Set<Product> products;


    public Category(String name) {
        this.name = name;
    }

}
