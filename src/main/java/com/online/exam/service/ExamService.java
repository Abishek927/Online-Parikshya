package com.online.exam.service;


import com.online.exam.model.Exam;

import java.util.List;

public interface ExamService {
    Exam createExam(Long userId,Long facId,Long catId,Long courseId,Exam exam) throws Exception;
    String deleteExam(Long userId,Long facId,Long catId,Long courseId,Long examId) throws Exception;
    Exam updateExam(Long userId,Long facId,Long catId,Long courseId,Long examId,Exam exam) throws Exception;
    List<Exam> getExamByCourse(Long userId,Long facId,Long catId,Long courseId) throws Exception;

}
