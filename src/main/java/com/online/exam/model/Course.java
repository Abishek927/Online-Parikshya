package com.online.exam.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="course_table")
@SQLDelete(sql = "UPDATE course_table C set C.deleted=true where C.course_id=?")
@Where(clause = "deleted=false")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="course_id")
    private Long courseId;
    @Column(name="course_title",unique = true,nullable = false)
    private String courseTitle;
    @Column(name="course_desc")
    private String courseDesc;
    @Column(name="course_created")
    private Date courseCreated;
    @Column(name = "deleted")
    private Boolean deleted=Boolean.FALSE;



    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "course")
    @JsonManagedReference(value = "course_table")
    private List<Exam> exams;

    @ManyToMany(mappedBy = "courses")
    private Set<User> users;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "course")
    @JsonManagedReference(value = "course_table")
    private List<Question> questions;



}
