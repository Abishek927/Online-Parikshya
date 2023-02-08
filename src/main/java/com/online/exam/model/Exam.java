package com.online.exam.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "exam_table")
@SQLDelete(sql = "UPDATE exam_table e set e.deleted=true where e.exam_id=?")
@Where(clause = "deleted=false")
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
    private Long examTimeLimit;
    @Column(name="exam_question_display_limit")
    private int examQuestionDisplayLimit;
    @Column(name="exam_started_time")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date examStartedTime;

    @Column(name="exam_ended_time")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date examEndedTime;
    @Column(name="exam_status")
    private Boolean examStatus=Boolean.TRUE;

    @Column(name="exam_total_marks")
    private int examTotalMarks;
    @Enumerated(EnumType.ORDINAL)
    private QuestionPattern questionPattern;

    private Boolean deleted=Boolean.FALSE;



    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "exam")
    @JsonManagedReference(value = "exam_table")
    private List<ExamAttempt> examAttemptList;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="course_id_fk",referencedColumnName = "course_id")
    @JsonBackReference(value = "course_table")
    private Course course;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "exam")
    @JsonManagedReference(value = "exam_table")
    private List<ExamQuestion> examquestions;





}
