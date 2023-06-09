package com.online.exam.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "result_table")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "result_id")
    private Integer resultId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="student_id_fk",referencedColumnName = "user_id")
    private User user;
    @Column(name="marks_obtained")
    private int marksObtained;
    @Column(name="result_status")
    private String resultStatus;
    @Column(name="correct_choice")
    private Integer correctChoice;
    private float percentage;
    @Column(name = "exam_conducted_date")
    private Date examConductedDate;//the date on which the exam was conducted
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exam_id_fk",referencedColumnName = "exam_id")
    private Exam exam;


}
