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
@Table(name = "reslt")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "result_id")
    private Integer resultId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="student_id_fk",referencedColumnName = "user_id")
    private User user;
    private Integer marksObtained;
    private String resultStatus;
    private LocalDate date;//the date on which the exam was conducted
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exam_id_fk",referencedColumnName = "exam_id")
    private Exam exam;


}
