package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="semester_table")
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="sem_id")
    private Long semesterId;
    @Column(name="sem_name",unique = true,nullable = false)
    private String semesterName;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "sem")
    @JsonManagedReference(value = "semester_table")
    private List<Course> courseList;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="faculty_id_fk",referencedColumnName = "faculty_id")
    @JsonBackReference(value = "faculty_table")
    private Faculty faculty;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "sem")
    @JsonManagedReference(value = "sem_table")
    private List<User> users;

}
