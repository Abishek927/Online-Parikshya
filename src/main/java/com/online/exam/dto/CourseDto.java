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
    private String courseName;
    private String courseDesc;
    private CategoryDto categoryDto;
    private List<Exam> exams;

}
