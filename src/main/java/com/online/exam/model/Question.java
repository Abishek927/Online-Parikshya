package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="question_table")

public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="question_id")
    private Long questionId;
    @Column(name="question_title",nullable = false,unique = true)
    private String questionTitle;

    @Column(name="question_ch_1")
    private String choice1;
    @Column(name="question_ch_2")
    private String choice2;
    @Column(name="question_ch_3")
    private String choice3;
    @Column(name="question_ch_4")
    private String choice4;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id_fk",referencedColumnName = "exam_id")
    @JsonBackReference(value = "exam_table")
    private Exam exam;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="answer_id_fk",referencedColumnName = "answer_id")
   private Answer answer;



}
