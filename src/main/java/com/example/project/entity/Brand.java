package com.example.project.entity;

import com.example.project.converter.CapitalizeCharConverter;
import com.example.project.validator.Alphabetic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Table(name = "brands")
public class Brand implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id", nullable = false)
    private int id;


    @Column(name = "brand_name", nullable = false, unique = true)
    @NotEmpty(message = "Brand came should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for brand name")
    @Alphabetic(message = "Only alphabetic word are allowed separated optionally by one space for brand name")
    @Size(min = 3, max = 25, message = "Brand name length should be between 3 and 25")
    @Convert(converter = CapitalizeCharConverter.class)
    private String name;


    @Column(name = "brand_picture")
    private String pictureUrl;


    @Transient
    @JsonIgnore
    private MultipartFile file;


    @Transient
    @JsonIgnore
    @OneToMany(targetEntity = Product.class, mappedBy = "brand", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ToString.Exclude
    private Set<Product> products;


    public Brand(String name, String pictureUrl) {
        this.name = name;
        this.pictureUrl = pictureUrl;
    }

}
