package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name="answer_table")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="answer_id")
    private Long answerId;
    @Column(name="answer_content",nullable = false)
    private String answerContent;
    @Column(name="answer_created",nullable = false)
    private Date answerCreated;
    @Column(name="answer_status",nullable = false)
    private String answerStatus;



}
