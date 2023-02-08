package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "exam_question")
public class ExamQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="exam_question_id")
    private Long examQuestionId;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id_fk",referencedColumnName = "exam_id")
    @JsonBackReference(value = "exam_table")
    private Exam exam;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="question_id_fk",referencedColumnName = "question_id")
    @JsonBackReference(value = "question_table")
    private Question question;
}
