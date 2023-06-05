package com.online.exam.helper;

import com.online.exam.model.Exam;
import com.online.exam.repo.ExamRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class PassMarkCalculator {

    public static Integer getPassMarks(Exam exam){
       return exam.getExamTotalMarks()/2;
    }


}
