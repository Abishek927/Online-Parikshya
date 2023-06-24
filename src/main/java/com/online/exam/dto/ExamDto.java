package com.online.exam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ExamDto {
    private Long examId;
    private String examTitle;
    private String examDesc;
    private int examQuestionDisplayLimit;
    private Boolean examStatus;
    @Column(name="exam_started_time")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date examStartedTime;
    @Column(name="exam_ended_time")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date examEndedTime;
    private String questionDifficultyType;
    private String questionPattern;
    private Integer totalMarks;
    private List<QuestionDto> questionDtos;
    private Long examTotalTime;
}
