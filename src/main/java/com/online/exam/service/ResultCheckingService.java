package com.online.exam.service;

import com.online.exam.model.Result;
import com.online.exam.model.StudentExamAnswer;

import java.text.ParseException;
import java.util.Map;

public interface ResultCheckingService {//sea->studentexamanswer
    Map<String,Object> calculateResultProperties(StudentExamAnswer studentExamAnswer) throws ParseException;

}
