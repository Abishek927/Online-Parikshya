package com.online.exam.helper;

import com.online.exam.model.*;
import com.online.exam.repo.ExamRepo;
import com.online.exam.repo.QuestionRepo;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Component
public class CheckStudentAnswerChoice {
    private QuestionRepo questionRepo;

    private ExamRepo examRepo;

    public CheckStudentAnswerChoice(QuestionRepo questionRepo, ExamRepo examRepo) {
        this.questionRepo = questionRepo;
        this.examRepo = examRepo;
    }

    public Integer calculateMarksObtained(StudentExamAnswer studentExamAnswer){
        Integer totalMarksObtained=0;
        List< SubmitAnswer> submitAnswers =studentExamAnswer.getSubmitAnswers();
        for (SubmitAnswer eachSubmitAnswer:submitAnswers
             ) {
            if(checkWhetherSubmittedChoiceCorrectOrNot(eachSubmitAnswer.getAnswerContent(), eachSubmitAnswer.getQuestionId())){
                totalMarksObtained=totalMarksObtained+getMarksForEachQuestion(eachSubmitAnswer.getQuestionId());
            }

        }
        return totalMarksObtained;
    }

    public boolean checkWhetherSubmittedChoiceCorrectOrNot(String submitAnswer,Long qusId){
        boolean status=false;
        Question question =questionRepo.findById(qusId).get();
        String selectedAnswer=submitAnswer;
        String realAnswer=question.getAnswer().getAnswerContent();
        if(selectedAnswer.equals(realAnswer)){
            status=true;
        }
        return status;
    }
    private Integer getMarksForEachQuestion(Long qusId){
        Question question=questionRepo.findById(qusId).get();
        return question.getQuestionMarks();
    }
    public String checkWhetherPassOrFail(StudentExamAnswer studentExamAnswer){
        Exam retrievedExam=studentExamAnswer.getExam();
        Integer passMarks=PassMarkCalculator.getPassMarks(retrievedExam);
        Integer obtainedMarks=calculateMarksObtained(studentExamAnswer);
        if(obtainedMarks<passMarks){
            return PassStatus.FAIL.toString();
        }
        return PassStatus.PASS.toString();
    }

    public float calculatePercentage(StudentExamAnswer studentExamAnswer){
        float marksObtained=calculateMarksObtained(studentExamAnswer);
        float totalMarks=studentExamAnswer.getExam().getExamTotalMarks();
        return ((marksObtained/totalMarks)*100);
    }
    public Integer correctChoice(StudentExamAnswer studentExamAnswer){
        Integer correctCount=0;
        for (SubmitAnswer eachSubmitAnswer:
        studentExamAnswer.getSubmitAnswers()) {
            if(checkWhetherSubmittedChoiceCorrectOrNot(eachSubmitAnswer.getAnswerContent(), eachSubmitAnswer.getQuestionId())){
                correctCount=correctCount+1;
            }

        }
        return correctCount;
    }

    public Date getExamConductedDate(Exam retrievedExam) throws ParseException {

        DateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        return formatter.parse(formatter.format(retrievedExam.getExamStartedTime()));
    }


}
