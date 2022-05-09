package com.example.project.entity;

import com.example.project.converter.CapitalizeCharConverter;
import com.example.project.validator.Alphabetic;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private int id;


    @Column(name = "role_name", nullable = false, unique = true)
    @NotEmpty(message = "Role name should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for role name")
    @Size(min = 3, max = 25, message = "Role name length should be between 3 and 25")
    @Alphabetic(message = "Only alphabetic word are allowed separated optionally by one space for role name")
    @Convert(converter = CapitalizeCharConverter.class)
    private String name;


    public Role(String name) {
        this.name = name;
    }

}
