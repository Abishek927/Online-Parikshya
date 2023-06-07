package com.online.exam.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_table")
@SQLDelete(sql = "UPDATE user_table u set u.deleted=true where u.user_id=?")
@Where(clause = "deleted=false")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "user_name", nullable = false)
    private String userName;
    @Column(name = "user_email", unique = true, nullable = false)
    private String userEmail;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "user_roll_no")
    private Long userRollNo;
    @Column(name = "user_contact_number")
    private String userContactNumber;
    @Column(name = "user_gender")
    private String userGender;
    private Boolean deleted = Boolean.FALSE;
    @Column(name = "user_date_of_birth")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate userDateOfBirth;
    private String userStatus;
    private Boolean isEnabled = Boolean.FALSE;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id_fk", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id_fk", referencedColumnName = "role_id")
    )
    private Set<Role> userRoles;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference(value = "user_table")
    private List<ExamAttempt> examAttempts;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_course",
            joinColumns = @JoinColumn(name = "user_id_fk", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id_fk", referencedColumnName = "course_id")
    )
    private Set<Course> courses;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference(value = "user_table")
    private List<Feedback> feedbacks;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference(value = "user_table")
    private List<Question> questions;

    @ManyToMany(mappedBy = "user")
    private Set<Exam> exams;


}

