package com.online.exam.service;


import com.online.exam.model.Exam;

import java.security.Principal;
import java.util.List;

public interface ExamService {
    Exam createExam(Long userId,Long courseId,Exam exam) throws Exception;
    String deleteExam(Long userId, Long examId, Principal principal) throws Exception;
    Exam updateExam(Long userId,Long examId,Exam exam) throws Exception;
    List<Exam> getExamByCourse(Long userId,Long courseId) throws Exception;

}
