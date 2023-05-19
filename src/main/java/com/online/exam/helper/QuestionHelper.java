package com.online.exam.helper;

import com.online.exam.dto.AnswerDto;
import com.online.exam.dto.QuestionDto;
import com.online.exam.model.Answer;
import com.online.exam.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionHelper {

    public static QuestionDto getQuestionDto(Question question){

            QuestionDto questionDto=new QuestionDto();
            questionDto.setQuestionId(question.getQuestionId());
            questionDto.setQuestionTitle(question.getQuestionTitle());
            questionDto.setQuestionMarks(question.getQuestionMarks());
            questionDto.setAnswerChoice(question.getAnswerChoice());
            questionDto.setChoice1(question.getChoice1());
            questionDto.setChoice2(question.getChoice2());
            questionDto.setChoice3(question.getChoice3());
            questionDto.setChoice4(question.getChoice4());
            questionDto.setAnswerDto(getAnserDto(question.getAnswer()));
            return questionDto;

    }
    public static AnswerDto getAnserDto(Answer answer){
        AnswerDto  answerDto=new AnswerDto();
        answerDto.setAnswerContent(answer.getAnswerContent());
        return answerDto;
    }
    public static List<QuestionDto> getAllQuestionDto(List<Question> questionList){
        List<QuestionDto> questionDtos=new ArrayList<>();
        for (Question eachQuestion:questionList
        ) {
            questionDtos.add(getQuestionDto(eachQuestion));
        }
        return questionDtos;

    }

}
