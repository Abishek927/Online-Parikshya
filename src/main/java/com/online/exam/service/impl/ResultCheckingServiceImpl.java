package com.online.exam.service.impl;

import com.online.exam.helper.CheckStudentAnswerChoice;
import com.online.exam.model.StudentExamAnswer;
import com.online.exam.service.ResultCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
@Service
public class ResultCheckingServiceImpl implements ResultCheckingService {
    @Autowired
    private CheckStudentAnswerChoice checkStudentAnswerChoice;

    @Override
    public Map<String, Object> calculateResultProperties(StudentExamAnswer studentExamAnswer) throws ParseException {
        Map<String,Object> resultProperties=new HashMap<>();
        resultProperties.put("marksObtained",checkStudentAnswerChoice.calculateMarksObtained(studentExamAnswer));
        resultProperties.put("resultStatus",checkStudentAnswerChoice.checkWhetherPassOrFail(studentExamAnswer));
        resultProperties.put("correctChoice",checkStudentAnswerChoice.correctChoice(studentExamAnswer));
        resultProperties.put("percentage",checkStudentAnswerChoice.calculatePercentage(studentExamAnswer));
        resultProperties.put("examConductedDate",checkStudentAnswerChoice.getExamConductedDate(studentExamAnswer.getExam()));

        return resultProperties;
    }


}
