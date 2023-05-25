package com.online.exam.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Getter
@Setter
public class SubmitAnswerDto {
    private Long examId;
    private Long studentId;
    private Long courseId;
    private List<SelectedChoiceDto> selectedChoiceDtos;

}
