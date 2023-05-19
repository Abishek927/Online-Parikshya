package com.online.exam.service.impl;


import com.online.exam.dto.AnswerDto;
import com.online.exam.dto.QuestionDto;
import com.online.exam.helper.QueryHelper;
import com.online.exam.helper.QuestionHelper;
import com.online.exam.model.*;
import com.online.exam.repo.AnswerRepo;

import com.online.exam.repo.QuestionRepo;
import com.online.exam.repo.UserRepo;
import com.online.exam.service.QuestionService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QueryHelper queryHelper;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private AnswerRepo answerRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public String createQuestion(QuestionDto questionDto, Principal principal) throws Exception {
        User retrievedUser = userRepo.findByUserEmail(principal.getName());
        Course retrievedCourse = this.queryHelper.getCourseMethod(questionDto.getCourseId());
        if (retrievedUser.getCourses().contains(retrievedCourse)) {
            Question question=getQuestion(questionDto);
            Answer answer = new Answer();
                Map<String,String> answerChoice=getAnswerChoice(question);
            for (Map.Entry<String, String> entry : answerChoice.entrySet()) {
                  answer.setAnswerContent(entry.getValue());
                question.setAnswerChoice(entry.getKey());
            }
                answer.setAnswerStatus("active");
                answer.setAnswerCreated(new Date());
                answer = answerRepo.save(answer);
                question.setAnswer(answer);
                question.setCourse(retrievedCourse);
                question.setUser(retrievedUser);
                this.questionRepo.save(question);
        }
        return "question created successfully";
    }


    @Override
    public Map<Integer,String> updateQuestion(QuestionDto questionDto, Principal principal) throws Exception {
        Map<Integer,String> message=new HashMap<>();
        User retrievedUser =userRepo.findByUserEmail(principal.getName());
        Question retrievedQuestion = this.queryHelper.getQuestionMethod(questionDto.getQuestionId());
        if(retrievedQuestion.getUser().equals(retrievedUser)) {
                    if (!questionDto.getQuestionTitle().isEmpty()) {
                            retrievedQuestion.setQuestionTitle(questionDto.getQuestionTitle());
                        }
                        if (questionDto.getQuestionMarks() >0) {
                            retrievedQuestion.setQuestionMarks(questionDto.getQuestionMarks());
                        }
                        else{
                            message.put(500,"Invalid marks!!!");
                            return message;
                        }

                        String updatedAnswer = questionDto.getAnswerChoice();
                        if (!updatedAnswer.equals(retrievedQuestion.getAnswerChoice())||checkWhetherTheContentChange(retrievedQuestion,getAnswerChoice(getQuestion(questionDto)))){
                            Answer answer = retrievedQuestion.getAnswer();
                            Map<String,String> answerChoice=getAnswerChoice(getQuestion(questionDto));
                            for (Map.Entry<String, String> entry : answerChoice.entrySet()) {
                                answer.setAnswerContent(entry.getValue());
                                retrievedQuestion.setAnswerChoice(entry.getKey());
                            }
                                answer.setAnswerCreated(new Date());
                                answerRepo.save(answer);

                        }
                        this.questionRepo.save(retrievedQuestion);
                        message.put(200,"Question updated successfully");
        }
        return message;
    }

    @Override
    public List<QuestionDto> getQuestionByCourse(Long courseId,Principal principal)  {
        List<Question> questions = new ArrayList<>();
        User retrievedUser=userRepo.findByUserEmail(principal.getName());
        Course retrievedCourse = this.queryHelper.getCourseMethod(courseId);
        if(retrievedCourse.getUsers().contains(retrievedUser)) {
            questions = this.questionRepo.findByCourse(retrievedCourse);
           if(questions.isEmpty()){
               return null;
           }

        }
        return QuestionHelper.getAllQuestionDto(questions);
    }

    @Override
    public QuestionDto getQuestionById(Long id,Principal principal) {
        Question retrievedQuestion=queryHelper.getQuestionMethod(id);

        if(retrievedQuestion.getUser().equals(userRepo.findByUserEmail(principal.getName()))){
            return QuestionHelper.getQuestionDto(retrievedQuestion);
        }
        return null;
    }

    @Override
    public String deleteQuestion(Long qusId,Principal principal){
        String resultMessage = "";
        User loggedInUser=userRepo.findByUserEmail(principal.getName());
        Question retrievedQuestion = this.queryHelper.getQuestionMethod(qusId);
        if(retrievedQuestion.getUser().equals(loggedInUser)) {
            this.questionRepo.deleteQuestionByQuestionId(retrievedQuestion.getQuestionId());
            resultMessage = "question deleted successfully for the id " + retrievedQuestion.getQuestionId();
        }
        return resultMessage;
    }

    private Question getQuestion(QuestionDto questionDto){
        Question question=new Question();
        question.setQuestionTitle(questionDto.getQuestionTitle());
        question.setChoice1(questionDto.getChoice1());
        question.setChoice2(questionDto.getChoice2());
        question.setChoice3(questionDto.getChoice3());
        question.setChoice4(questionDto.getChoice4());
        question.setQuestionMarks(questionDto.getQuestionMarks());
        question.setAnswerChoice(questionDto.getAnswerChoice().toString());
        return question;
    }
    private Map<String,String> getAnswerChoice(Question question) throws Exception {
        Map<String,String> answerChoice=new HashMap<>();
        switch (question.getAnswerChoice()){
            case "choice1":
                answerChoice.put("choice1",question.getChoice1());
                break;
            case "choice2":
                answerChoice.put("choice2",question.getChoice2());
                break;
            case "choice3":
                answerChoice.put("choice3",question.getChoice3());
                break;
            case "choice4":
                answerChoice.put("choice4",question.getChoice4());
                break;
            default:
                throw new Exception("Invalid user choice!!!");

        }
        return answerChoice;

    }
    private boolean checkWhetherTheContentChange(Question question,Map<String,String> choice){
        boolean status=false;
        for (Map.Entry<String, String> entry : choice.entrySet()) {
            if(!question.getAnswer().getAnswerContent().equals(entry.getValue())){
                status=true;
            }
        }
        return status;
    }





}
