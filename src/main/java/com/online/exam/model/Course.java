package com.online.exam.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="course_table")
@SQLDelete(sql = "UPDATE course_table c set c.deleted=true where c.course_id=?")
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
    private List<User> userList;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "course")
    @JsonManagedReference(value = "course_table")
    private List<Exam> exams;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "cat_id_fk",referencedColumnName = "cat_id")
    @JsonBackReference(value = "category_table")
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="user_id_fk",referencedColumnName = "user_id")
    @JsonBackReference(value = "user_table")
    private User user;

}
