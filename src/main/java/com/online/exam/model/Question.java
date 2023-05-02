package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @Column(name="question_title")
    private String questionTitle;

    @Column(name="question_ch_1")
    private String choice1;
    @Column(name="question_ch_2")
    private String choice2;
    @Column(name="question_ch_3")
    private String choice3;
    @Column(name="question_ch_4")
    private String choice4;

    @Column(name="question_marks",nullable = false)
    private int questionMarks;

    @Enumerated(EnumType.ORDINAL)
    private AnswerChoice answerChoice;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id_fk",referencedColumnName = "course_id")
    @JsonBackReference(value = "course_table")
    private Course course;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="answer_id_fk",referencedColumnName = "answer_id")
    private Answer answer;
  @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "question")
  @JsonManagedReference(value = "question_table")
  private List<ExamQuestion> examQuestions;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id_fk",referencedColumnName = "user_id")
    @JsonBackReference(value = "user_table")
    private User user;






}
