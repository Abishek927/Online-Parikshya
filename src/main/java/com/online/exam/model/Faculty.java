package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Entity

@Data
@Table(name="faculty_table")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="faculty_id")
    private Long facultyId;
    @Column(name="faculty_name",nullable = false,unique = true)
    private String facultyName;
    @Column(name="faculty_desc",nullable = false)
    private String facultyDesc;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "faculty")
    @JsonManagedReference(value = "faculty_table")
    private List<Semester> semesterList;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "faculty")
    @JsonManagedReference(value = "faculty_table")
    private List<User> users;
}
