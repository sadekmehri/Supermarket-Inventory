package com.example.project.entity;

import com.example.project.key.AccountKeys;
import com.example.project.validator.PasswordComplexity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "accounts")
@IdClass(AccountKeys.class)
public class Account {

    @Id
    @Column(name = "staff_id", nullable = false)
    private int staffId;


    @Id
    @Column(name = "role_id", nullable = false)
    private int roleId;


    @Email
    @NotEmpty(message = "Email should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for email")
    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @PasswordComplexity
    @NotEmpty(message = "Password should not be empty")
    @NotBlank(message = "Empty spaces are not allowed for password")
    @Column(name = "password", nullable = false)
    private String password;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staff_id", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    private Staff staff;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false, insertable = false, updatable = false)
    @ToString.Exclude
    private Role role;


    public Account(int staffId, int roleId, String email, String password) {
        this.staffId = staffId;
        this.roleId = roleId;
        this.email = email;
        this.password = password;
    }

}
