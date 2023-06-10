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
    private Integer marksObtained;
    private String resultStatus;
    private Integer correctChoice;
    private float percentage;
    private Date examConductedDate;

}
