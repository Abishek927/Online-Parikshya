package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name="user_faculty_table")
public class UserFaculty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userFacultyId;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id_fk",referencedColumnName = "user_id")
    @JsonBackReference(value = "user_table")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="faculty_id_fk",referencedColumnName = "faculty_id")
    @JsonBackReference(value = "faculty_table")
    private Faculty faculty;


}
