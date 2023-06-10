package com.online.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto {
    private Integer resultId;
    private String studentName;
    private String  examTitle;
    private String courseName;
    private int marksObtained;
    private String resultStatus;
    private int correctChoice;
    private float percentage;
    private Date examConductedDate;

}
