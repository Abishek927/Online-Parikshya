package com.online.exam.service;


import com.online.exam.dto.QuestionDto;
import com.online.exam.model.Question;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface QuestionService {
    String createQuestion(QuestionDto questionDto, Principal principal) throws Exception;
    Map<Integer,String> updateQuestion(QuestionDto questionDto, Principal principal) throws Exception;
    List<QuestionDto> getQuestionByCourse(Long courseId,Principal principal);
    QuestionDto getQuestionById(Long id,Principal principal);
    String deleteQuestion(Long qusId,Principal principal) throws Exception;

}
