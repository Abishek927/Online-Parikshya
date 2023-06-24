package com.online.exam.dto;

import com.online.exam.model.Answer;
import com.online.exam.model.AnswerChoice;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionDto {
    private Long questionId;
    private String questionTitle;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String difficultyLevel;
    private int questionMarks;
    private String answerChoice;
    private Long courseId;
    private AnswerDto answerDto;
}
