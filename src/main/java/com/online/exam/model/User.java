package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;


import java.util.List;

@Entity
@Data

@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    private Long userId;
    @Column(name="user_name",unique = true,nullable = false)
    private String userName;
    @Column(name="user_password",nullable = false)
    private String userPassword;
    @Column(name="user_first_name")
    private String userFirstName;
    @Column(name="user_last_name")
    private String userLastName;
    @Column(name="user_email",nullable = false)
    private String userEmail;
    @Column(name="user_phone_number",nullable = false)
    private String userPhoneNumber;
    private boolean enabled=true;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "user")
    @JsonManagedReference(value = "user_role_table")
    private List<UserRole> userRoles;

}
