package com.online.exam.helper;

import com.online.exam.model.Question;
import com.online.exam.model.QuestionPattern;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ExamHelper {
    public Boolean generateValidDate(Date startDate, Date endDate) {
        Date currentDate = new Date();
        int num1 = currentDate.compareTo(startDate);
        int num2 = endDate.compareTo(startDate);
        if (num1 >= 0 && num2 > 0) {
            return Boolean.TRUE;

        }
        return Boolean.FALSE;

    }

    public Long generateTotalExamTime(Date startDate, Date endDate) {
        Long totalTime = null;
        if (generateValidDate(startDate, endDate)) {
            totalTime = endDate.getTime() - startDate.getTime();
        }
        return totalTime;
    }

    public List<Question> generateQuestion(List<Question> questions, int questionLimitSize, QuestionPattern questionPattern) throws Exception {
        List<Question> resultedQuestion = new ArrayList<>();
        HelperClass helperClass = new HelperClass();

        if (questionPattern.equals(QuestionPattern.random)) {
            if (!questions.isEmpty()) {
                resultedQuestion = helperClass.generateRandomQuestion(questions, questionLimitSize);

            } else {
                throw new Exception("there is no questions!!!!");
            }
            return resultedQuestion;
        } else if (questionPattern.equals(QuestionPattern.sort)) {
            if (!questions.isEmpty()) {
                resultedQuestion = helperClass.generateSortedQuestion(questions, questionLimitSize);
            } else {
                throw new Exception("there is no questions!!!");
            }
            return resultedQuestion;

        }
        return resultedQuestion;

    }




    public int generateTotalMarks(List<Question> questions, int questionLimitSize, QuestionPattern questionPattern) throws Exception {
        int totalMarks = 0;
        List<Question> questions1 = generateQuestion(questions, questionLimitSize, questionPattern);
        if (!questions1.isEmpty()) {
            for (Question eachQuestion : questions1
            ) {
                int retrievedMarks = eachQuestion.getQuestionMarks();
                totalMarks = totalMarks + retrievedMarks;


            }
            return totalMarks;
        } else {
            throw new Exception("Please select the appropriate question pattern");
        }

    }











}
