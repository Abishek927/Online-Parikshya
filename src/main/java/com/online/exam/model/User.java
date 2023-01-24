package com.online.exam.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_table")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")

    private Long userId;

    @Column(name="user_name",nullable = false)
private String userName;
    @Column(name="user_email",unique = true,nullable =false)
private String userEmail;
    @Column(name="user_password")
private String userPassword;
    @Column(name="user_roll_no")
private Long userRollNo;
    @Column(name="user_contact_number")
private String userContactNumber;
    @Column(name = "user_gender")
private String userGender;
    @Column(name="user_date_of_birth")
    @JsonFormat(pattern ="yyyy-MM-dd")
private LocalDate userDateOfBirth;




    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
    @JsonManagedReference(value = "user_table")
    private List<userRole> userRoles;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="user")
    @JsonManagedReference(value = "user_table")
    private List<ExamAttempt> examAttempts;


    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="course_id_fk",referencedColumnName = "course_id")
    @JsonBackReference(value = "course_table")
    private Course course;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
    @JsonManagedReference(value = "user_table")
   private List<UserFaculty> userFaculties;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
    @JsonManagedReference(value = "user_table")
    private List<Course> courses;


    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "cat_id_fk",referencedColumnName = "cat_id")
    @JsonBackReference(value = "category_table")
    private Category category;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
    @JsonManagedReference(value = "user_table")
    private List<Feedback> feedbacks;







}

