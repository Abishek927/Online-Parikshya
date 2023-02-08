package com.online.exam.helper;

import com.online.exam.model.Question;
import com.online.exam.repo.QuestionRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class HelperClass {
    @Autowired
    private QuestionRepo questionRepo;



    public List<Question> findAllQuestion(){
        return this.questionRepo.findAll();
    }



    public  List<Question>  generateRandomQuestion(List<Question> questions,int questionLimitSize) throws Exception {
        List<Question> randomGeneratedQuestion = new ArrayList<>(questionLimitSize);
        if (!questions.isEmpty()) {
            for (int i = 0; i < questions.size(); i++) {

                int result =(int)(Math.random()*randomGeneratedQuestion.size());
                if (!randomGeneratedQuestion.contains(questions.get(result))) {
                    randomGeneratedQuestion.add(questions.get(result));
                }


            }


        } else {
            throw new Exception("Empty list of question!!!!");
        }

return randomGeneratedQuestion;

    }


    public List<Question> generateSortedQuestion(List<Question> questions,int questionLimitSize){
        List<Question> sortedGeneratedQuestion=new ArrayList<>(questionLimitSize);
        Collections.sort(questions,new QuestionComparator());
        for(int i=0;i<questionLimitSize;i++){
            sortedGeneratedQuestion.add(questions.get(i));
        }
        return sortedGeneratedQuestion;
    }

}
