package com.example.project.entity;

import com.example.project.converter.CapitalizeCharConverter;
import com.example.project.validator.Alphabetic;
import com.example.project.validator.TodayDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "staffs")
public class Staff implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id", nullable = false)
    private int id;


    @Column(name = "first_name", nullable = false)
    @NotEmpty(message = "First name should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for first name")
    @Size(min = 3, max = 40, message = "First name length should be between 3 and 40")
    @Alphabetic(message = "Only alphabetic word are allowed separated optionally by one space for first name")
    @Convert(converter = CapitalizeCharConverter.class)
    private String firstName;


    @Column(name = "last_name", nullable = false)
    @NotEmpty(message = "Last name should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for last name")
    @Size(min = 3, max = 40, message = "Last name length should be between 3 and 40")
    @Alphabetic(message = "Only alphabetic word are allowed separated optionally by one space for last name")
    @Convert(converter = CapitalizeCharConverter.class)
    private String lastName;


    @Column(name = "date_of_birth", nullable = false)
    @TodayDate
    private LocalDate dob;


    @Column(name = "phone", nullable = false, unique = true)
    @NotEmpty(message = "Phone number should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for phone number")
    @Size(min = 8, max = 25, message = "Phone number length should be between 8 and 25")
    @Convert(converter = CapitalizeCharConverter.class)
    private String phone;


    @Column(name = "hire_date", nullable = false)
    @TodayDate
    private LocalDate hireDate;


    @Transient
    private int age;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    @ToString.Exclude
    private Store store;


    @JsonIgnore
    @ToString.Exclude
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(targetEntity = Order.class, mappedBy = "staff", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Set<Order> orders;


    public Staff(String firstName, String lastName, LocalDate dob, String phone, LocalDate hireDate, Store store) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.phone = phone;
        this.hireDate = hireDate;
        this.store = store;
    }

    public int getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

    @JsonIgnore
    public boolean isValidHireDate() {
        if (hireDate == null || dob == null) return false;
        return getAge() >= 12 && dob.plusYears(12).isBefore(hireDate);
    }

}
