package com.online.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto {
    private String studentName;
    private String  examTitle;
    private String courseName;
    private Integer marksObtained;
    private String resultStatus;
    private String correctChoice;
    private String percentage;
    private String examConductedDate;

}
