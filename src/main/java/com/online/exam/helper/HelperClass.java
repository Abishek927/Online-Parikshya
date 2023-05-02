package com.online.exam.helper;

import com.online.exam.model.Question;
import com.online.exam.repo.QuestionRepo;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@AllArgsConstructor
@Component
public class HelperClass {
    @Autowired
    private QuestionRepo questionRepo;


    public List<Question> findAllQuestion() {
        return this.questionRepo.findAll();
    }


    public List<Question> generateRandomQuestion(List<Question> questions, int questionLimitSize) throws Exception {
        List<Question> randomGeneratedQuestion = new ArrayList<>(questionLimitSize);
        if (!questions.isEmpty()) {
            outerLoop:
            for (int i = 0; i < questions.size(); i++) {

                int result = (int) (Math.random() * questions.size());
                if (randomGeneratedQuestion.contains(questions.get(result)) == true) {
                    continue;
                }
                randomGeneratedQuestion.add(questions.get(result));
                if (randomGeneratedQuestion.size() == questionLimitSize) {
                    break outerLoop;
                }


            }


        } else {
            throw new Exception("Empty list of question!!!!");
        }

        return randomGeneratedQuestion;

    }


    public List<Question> generateSortedQuestion(List<Question> questions, int questionLimitSize) throws Exception {
        if (questions.size() >= questionLimitSize) {
            List<Question> sortedGeneratedQuestion = new ArrayList<>(questionLimitSize);
            List<Question> questionNotInExam=questionRepo.findByQuestionNotInExam();
            if(!questionNotInExam.isEmpty()&&questionNotInExam.size()>=questionLimitSize)
            Collections.sort(questions, new QuestionComparator());
            for (int i = 0; i < questionLimitSize; i++) {
                sortedGeneratedQuestion.add(questions.get(i));
            }
            return sortedGeneratedQuestion;
        }
        else

        {
            throw new Exception("Invalid question limit!!!!");
        }
    }

}


