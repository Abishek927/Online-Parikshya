package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name="category_table")
@SQLDelete(sql = "UPDATE category_table c set c.deleted=true where c.cat_id=?")
@Where(clause = "deleted=false")

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cat_id")
    private Long categoryId;
    @Column(name="category_name",unique = true,nullable = false)
    private String categoryName;

    private boolean deleted=Boolean.FALSE;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "category")
    @JsonManagedReference(value = "category_table")
    private List<Course> courseList;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="faculty_id_fk",referencedColumnName = "faculty_id")
    @JsonBackReference(value = "faculty_table")
    private Faculty faculty;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "category")
    @JsonManagedReference(value = "category_table")
    private List<User> users;

}
