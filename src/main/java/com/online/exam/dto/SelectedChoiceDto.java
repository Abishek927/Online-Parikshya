package com.online.exam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectedChoiceDto {
    private Long questionId;
    private String selectedChoice;
}
