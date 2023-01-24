package com.online.exam.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "exam_table")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="exam_id")
    private Long examId;
    @Column(name = "exam_title",unique = true,nullable = false)
    private String examTitle;
    @Column(name="exam_desc")
    private String examDesc;
    @Column(name="exam_time_limit")
    private String examTimeLimit;
    @Column(name="exam_question_display_limit")
    private int examQuestionDisplayLimit;
    @Column(name="exam_created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime examCreated;
    @Column(name="exam_status")
    private String examStatus;



    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "exam")
    @JsonManagedReference(value = "exam_table")
    private List<ExamAttempt> examAttemptList;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="course_id_fk",referencedColumnName = "course_id")
    @JsonBackReference(value = "course_table")
    private Course course;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "exam")
    @JsonManagedReference(value = "exam_table")
    private List<Question> questions;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "exam")
    @JsonManagedReference(value = "exam_table")
    private List<Answer> answers;



}
