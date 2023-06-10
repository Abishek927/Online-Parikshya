package com.online.exam.service;


import com.online.exam.dto.ExamDto;
import com.online.exam.dto.SubmitAnswerDto;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface ExamService {
    Map<Integer,String> createExam(ExamDto examDto,Principal principal) throws Exception;
    Map<Integer,String>deleteExam(Long examId, Principal principal) throws Exception;
    List<ExamDto> getExamByCourse(Long courseId);
    ExamDto getExamById(Long examId);
    Map<Integer,String> updateExam(ExamDto examDto, Principal principal) throws Exception;
    ExamDto startExam(Long examId,Principal principal);

    Map<String,Object> submitExam(SubmitAnswerDto submitAnswerDto,Principal principal) throws Exception;


}
