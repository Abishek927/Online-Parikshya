package com.online.exam.helper;
import com.online.exam.model.Question;
import com.online.exam.model.TitleComparator;
import com.online.exam.model.User;
import com.online.exam.repo.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
public class HelperClass {
    private QuestionRepo questionRepo;

    private User user;
    private MergeSort mergeSort;
    @Autowired
    public HelperClass(MergeSort mergeSort,User user,QuestionRepo questionRepo) {
    this.questionRepo=questionRepo;
        this.mergeSort=mergeSort;
        this.user=user;
    }

    public List<Question> findAllQuestionByUser(User user
    ) {
        return this.questionRepo.findByUser(user);
    }
    public List<Question> generateRandomQuestion(List<Question> questions, int questionLimitSize) throws Exception {
        List<Question> randomGeneratedQuestion = new ArrayList<>(questionLimitSize);
        randomGeneratedQuestion=generateRandomQuestionIndexAndGenerateQuestion(questions,questionLimitSize,randomGeneratedQuestion);

        /*  outerLoop:
            for (int i = 0; i < questions.size(); i++) {

                int result = (int) (Math.random() * questions.size());
                //Random random=new Random();//implementation of mersenne twister algorithm
                //int result= random.nextInt(questions.size());
                if (randomGeneratedQuestion.contains(questions.get(result)) == true) {
                    continue;
                }
                randomGeneratedQuestion.add(questions.get(result));
                if (randomGeneratedQuestion.size() == questionLimitSize) {
                    break outerLoop;
                }


            }*/
            return randomGeneratedQuestion;

    }


    public List<Question> generateSortedQuestion(List<Question> questions, int questionLimitSize,User user,MergeSort mergeSort,QuestionRepo questionRepo) throws Exception {
        List<Question> sortedGeneratedQuestion = new ArrayList<>(questionLimitSize);
        HelperClass helperClass=new HelperClass(mergeSort,user,questionRepo);
        if(helperClass.findAllQuestionByUser(user).size()>=questionLimitSize) {
            if (questionRepo.findBySelected(true).isEmpty()) {
                sortedGeneratedQuestion.addAll(setSortedQuestion(questions, questionLimitSize));
            } else {
                if (questionRepo.countQuestionBySelectedFlag(false) >= questionLimitSize) {
                    List<Question> retrievedQuestion = questionRepo.findBySelected(false);
                    sortedGeneratedQuestion.addAll(setSortedQuestion(retrievedQuestion, questionLimitSize));
                } else if (questionRepo.countQuestionBySelectedFlag(true) == questionRepo.findAll().size()) {
                    List<Question> questions1 = questionRepo.findByUser(user);
                    for (Question eachQuestion : questions1
                    ) {
                        eachQuestion.setSelected(false);
                        questionRepo.save(eachQuestion);
                    }
                    sortedGeneratedQuestion.addAll(setSortedQuestion(questions1, questionLimitSize));


                } else if (questionRepo.countQuestionBySelectedFlag(false) < questionLimitSize) {
                    List<Question> questions1 = questionRepo.findBySelected(true);
                    for (Question eachQuestion : questions1
                    ) {
                        eachQuestion.setSelected(false);
                        questionRepo.save(eachQuestion);

                    }
                    sortedGeneratedQuestion.addAll(setSortedQuestion(questions1, questionLimitSize));

                }
            }
        }
        return sortedGeneratedQuestion;

    }
    private List<Question> setSortedQuestion(List<Question> questions,int questionLimit){
        List<Question> resultSortedQuestion=new ArrayList<>();
        List<Question> sortedQuestion=mergeSort.mergeSort(questions,new TitleComparator());
        for (int i=0;i<questionLimit;i++){
            resultSortedQuestion.add(sortedQuestion.get(i));
            Question question=sortedQuestion.get(i);
            question.setSelected(true);
            questionRepo.save(question);
        }
        return resultSortedQuestion;
    }
    
    public List<Question> generateRandomQuestionIndexAndGenerateQuestion(List<Question> questions, int questionLimitSize,List<Question> generatedQuestion){

        GenerateRandomNumber generateRandomNumber=new GenerateRandomNumber();
        List<Long> randomIds=generateRandomNumber.generateRandomNumbers(questions.size(),questionLimitSize);
        for (Long eachRandomId:randomIds
             ) {
            if(generatedQuestion.contains(questions.get(eachRandomId.intValue()))==true){
                continue;
            }
            generatedQuestion.add(questions.get(eachRandomId.intValue()));
            if(generatedQuestion.size()==questionLimitSize){
                return generatedQuestion;
            }
        }
        if(generatedQuestion.size()<questionLimitSize){
            generateRandomQuestionIndexAndGenerateQuestion(questions,questionLimitSize,generatedQuestion);
        }
return generatedQuestion;

    }





    }




