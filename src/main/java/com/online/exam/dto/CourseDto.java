package com.online.exam.dto;

import com.online.exam.model.Exam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CourseDto {
    private Long courseId;
    private String courseTitle;
    private String courseDesc;
    private Long categoryId;
    private Long userId;
    private List<ExamDto> exams;

}
