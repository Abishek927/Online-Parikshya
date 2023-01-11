package com.online.exam.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
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
private String userDateOfBirth;




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




    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="faculty_id_fk",referencedColumnName ="faculty_id")
    @JsonBackReference(value = "faculty_table")
    private Faculty faculty;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
    @JsonManagedReference(value = "user_table")
    private List<Course> courses;


    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "sem_id_fk",referencedColumnName = "sem_id")
    @JsonBackReference(value = "sem_table")
    private Semester sem;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
    @JsonManagedReference(value = "user_table")
    private List<Feedback> feedbacks;







}

