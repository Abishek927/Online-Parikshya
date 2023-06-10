package com.online.exam.service.impl;

import com.online.exam.dto.ResultDto;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.Result;
import com.online.exam.model.StudentExamAnswer;
import com.online.exam.model.User;
import com.online.exam.repo.CourseRepo;
import com.online.exam.repo.ResultRepo;
import com.online.exam.repo.UserRepo;
import com.online.exam.service.ResultCheckingService;
import com.online.exam.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = {ParseException.class,RuntimeException.class})
public class ResultServiceImpl implements ResultService {
    @Autowired
    private QueryHelper queryHelper;
    @Autowired
    private ResultCheckingService resultCheckingService;
    @Autowired
    private ResultRepo resultRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CourseRepo courseRepo;

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
    public List<ResultDto> viewResult(Principal principal) {
        User loggedInUser=userRepo.findByUserEmail(principal.getName());
        List<Result> resultList=resultRepo.findByUser(loggedInUser);
        if(resultList==null){
            return null;
        }
        return resultList.stream().map(result -> getResult(result)).collect(Collectors.toList());
    }

    private Result setResultAccordingToProperties(Map<String,Object> resultProperties){
        Result result=new Result();
        for (Map.Entry<String,Object> eachEntry:resultProperties.entrySet()
             ) {
            switch (eachEntry.getKey()){
                case "marksObtained":
                    result.setMarksObtained(Integer.parseInt(eachEntry.getValue().toString()));
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

    public ResultDto getResult(Result result){
        ResultDto resultDto=new ResultDto();
        resultDto.setCourseName(resultRepo.findCourseName(result.getUser().getUserId()));
        resultDto.setExamTitle(result.getExam().getExamTitle());
        resultDto.setResultStatus(result.getResultStatus());
        resultDto.setResultId(result.getResultId());
        resultDto.setCorrectChoice(result.getCorrectChoice());
        resultDto.setPercentage(result.getPercentage());
        resultDto.setStudentName(result.getUser().getUserName());
        resultDto.setExamConductedDate(resultDto.getExamConductedDate());
        return resultDto;
    }
}
