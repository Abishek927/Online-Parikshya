package com.online.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class FacultyDto {
    private Long facultyId;
    private String facultyName;
    private List<CategoryDto> categoryDtoList;
}
