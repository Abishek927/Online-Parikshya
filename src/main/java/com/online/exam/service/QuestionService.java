package com.online.exam.service;


import com.online.exam.model.Question;

import java.util.List;

public interface QuestionService {
    Question createQuestion(Long userId,Long courseId,Question question) throws Exception;
    Question updateQuestion(Long userId,Long qusId, Question question) throws Exception;
    List<Question> getQuestionByCourse(Long userId, Long courseId);
    String deleteQuestion(Long userId,Long qusId) throws Exception;

}
