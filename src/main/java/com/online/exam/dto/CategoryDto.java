package com.online.exam.dto;

import com.online.exam.model.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
    private Long categoryId;
    private String categoryName;
   private Long facultyId;
   private Long userId;
   List<CourseDto> courseDtos;
}
