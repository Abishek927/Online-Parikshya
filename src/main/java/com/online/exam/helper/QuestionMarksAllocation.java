package com.online.exam.helper;

import com.online.exam.constant.MarksConstant;
import com.online.exam.model.Question;
import com.online.exam.repo.QuestionRepo;
import com.online.exam.repo.StudentExamAnswerRepo;
import com.online.exam.repo.SubmitAnswerRepo;

public class QuestionMarksAllocation {

    private SubmitAnswerRepo submitAnswerRepo;
    private QuestionRepo questionRepo;

    public QuestionMarksAllocation(SubmitAnswerRepo submitAnswerRepo, QuestionRepo questionRepo) {
        this.submitAnswerRepo = submitAnswerRepo;
        this.questionRepo = questionRepo;
    }

    public void checkAndSetQuestionMarksAccordingToComplexity(Question question){
        Integer correctAnswerCountForGivenQuestion= submitAnswerRepo.countTotalCorrectAnswerForGivenQuestion(question.getQuestionId());
        Integer wrongAnswerCountForGivenQuestion= submitAnswerRepo.countTotalFalseAnswerForGivenQuestion(question.getQuestionId());
        if(correctAnswerCountForGivenQuestion==0&&wrongAnswerCountForGivenQuestion==0){
            question.setQuestionMarks(MarksConstant.INITIAL_MARKS);
        } else if (correctAnswerCountForGivenQuestion>wrongAnswerCountForGivenQuestion) {
            question.setQuestionMarks(MarksConstant.INITIAL_MARKS);
        } else if (correctAnswerCountForGivenQuestion==wrongAnswerCountForGivenQuestion) {
            question.setQuestionMarks(MarksConstant.AVERAGE_MARKS);

        } else if (correctAnswerCountForGivenQuestion<wrongAnswerCountForGivenQuestion) {
            question.setQuestionMarks(MarksConstant.DIFFICULT_MARKS);

        }
        questionRepo.save(question);

    }

}
