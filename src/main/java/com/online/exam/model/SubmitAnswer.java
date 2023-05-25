package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "submit_answer_table")
public class SubmitAnswer {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;
    @Column(name="answer_content")
    private String answerContent;
    @Column(name="question_id")
    private Long questionId;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "sea_id_fk",referencedColumnName = "sea_id")
    @JsonBackReference(value = "student_exam_answer")
    private StudentExamAnswer studentExamAnswer;

}
