package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="exam_attempt_table")
public class ExamAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="exam_attempt_id")
    private Long examAttemptId;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="user_id_fk",referencedColumnName = "user_id")
    @JsonBackReference(value = "user_table")
    private User user;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="exam_id_fk",referencedColumnName = "exam_id")
    @JsonBackReference(value = "exam_table")
    private Exam exam;


}
