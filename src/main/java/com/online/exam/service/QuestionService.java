package com.online.exam.service;


import com.online.exam.model.Question;

import java.util.List;

public interface QuestionService {
    Question createQuestion(Long userId,Long facId,Long catId,Long courseId,Question question) throws Exception;
    Question updateQuestion(Long userId, Long facId, Long catId, Long courseId, Long qusId, Question question) throws Exception;
    List<Question> getQuestionByCourse(Long userId, Long facId, Long catId, Long courseId) throws Exception;
    String deleteQuestion(Long userId,Long facId,Long catId,Long courseId,Long qusId) throws Exception;

}
