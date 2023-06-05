package com.online.exam.service;

import com.online.exam.dto.ResultDto;
import com.online.exam.model.Result;

import java.text.ParseException;

public interface ResultService {
    String createResult(Long studentExamAnswerId) throws ParseException;
    ResultDto viewResult(Long examId,Long studentId);

}
