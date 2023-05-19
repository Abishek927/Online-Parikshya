package com.online.exam.helper;
import com.online.exam.model.Question;
import com.online.exam.model.User;
import com.online.exam.repo.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ExamHelper {
    @Autowired
    private QuestionRepo questionRepo;
    private User user;
    public ExamHelper(QuestionRepo questionRepo){
        this.questionRepo=questionRepo;
    }
    public Boolean generateValidDate(Date startDate, Date endDate) {

        Date currentDate = new Date();
        int num1 = startDate.compareTo(currentDate);
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

    public List<Question> generateQuestion(List<Question> questions, int questionLimitSize, String questionPattern) throws Exception {
        List<Question> resultedQuestion = new ArrayList<>();
        HelperClass helperClass = new HelperClass(questionRepo,user);
        switch (questionPattern) {
            case "random":
                resultedQuestion = helperClass.generateRandomQuestion(questions, questionLimitSize);
                break;
            case "sort":
                resultedQuestion = helperClass.generateSortedQuestion(questions, questionLimitSize);
                break;
        }
        return resultedQuestion;

    }


    public ExamHelper() {
    }

    public int generateTotalMarks(List<Question> questions) throws Exception {
        int totalMarks = 0;
            for (Question eachQuestion : questions
            ) {
                int retrievedMarks = eachQuestion.getQuestionMarks();
                totalMarks = totalMarks + retrievedMarks;
            }
            return totalMarks;


    }
}
