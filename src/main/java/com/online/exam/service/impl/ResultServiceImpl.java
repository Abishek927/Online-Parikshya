package com.online.exam.service.impl;

import com.online.exam.dto.ResultDto;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.Result;
import com.online.exam.model.StudentExamAnswer;
import com.online.exam.repo.ResultRepo;
import com.online.exam.repo.StudentExamAnswerRepo;
import com.online.exam.service.ResultCheckingService;
import com.online.exam.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

@Service
@Transactional(rollbackFor = {ParseException.class,RuntimeException.class})
public class ResultServiceImpl implements ResultService {
    @Autowired
    private QueryHelper queryHelper;
    @Autowired
    private ResultCheckingService resultCheckingService;
    @Autowired
    private ResultRepo resultRepo;

    @Override
    public String createResult(Long studentExamAnswerId) throws ParseException {
        StudentExamAnswer studentExamAnswer=queryHelper.getStudentExamAnswer(studentExamAnswerId);
        Map<String,Object> resultProperties=resultCheckingService.calculateResultProperties(studentExamAnswer);
        Result result=setResultAccordingToProperties(resultProperties);
        if(result==null){
            throw new RuntimeException("something went wrong!!!!");
        }
        result.setExam(studentExamAnswer.getExam());
        result.setUser(studentExamAnswer.getUser());
        resultRepo.save(result);
        return "result created successfully";
    }

    @Override
    public ResultDto viewResult(Long examId, Long studentId) {
        return null;
    }

    private Result setResultAccordingToProperties(Map<String,Object> resultProperties){
        Result result=new Result();
        for (Map.Entry<String,Object> eachEntry:resultProperties.entrySet()
             ) {
            switch (eachEntry.getKey()){
                case "marksObtained":
                    result.setMarksObtained(Integer.getInteger(eachEntry.getValue().toString()));
                    break;
                case "resultStatus":
                    result.setResultStatus(eachEntry.getValue().toString());
                    break;
                case "correctChoice":
                    result.setCorrectChoice(Integer.parseInt(eachEntry.getValue().toString()));
                    break;
                case "percentage":
                    result.setPercentage(Float.parseFloat(eachEntry.getValue().toString()));
                    break;
                case "examConductedDate":
                    result.setExamConductedDate((Date)eachEntry.getValue());
                    break;
                default:
                    break;
            }

        }
        return result;
    }
}
