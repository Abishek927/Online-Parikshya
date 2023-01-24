package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;
@Entity

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name="faculty_table")
@SQLDelete(sql = "UPDATE faculty_table f set f.deleted=true where f.faculty_id=?")
@Where(clause = "deleted=false")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="faculty_id")
    private Long facultyId;
    @Column(name="faculty_name",nullable = false,unique = true)
    private String facultyName;
    @Column(name="faculty_desc",nullable = false)
    private String facultyDesc;
    @Column(name="deleted")
    private boolean deleted=Boolean.FALSE;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "faculty")
    @JsonManagedReference(value = "faculty_table")
    private List<Category> categoryList;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "faculty")
    @JsonManagedReference(value = "faculty_table")
    private List<UserFaculty> userFaculties;
}
