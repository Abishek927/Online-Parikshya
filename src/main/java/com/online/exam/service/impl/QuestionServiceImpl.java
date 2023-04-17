package com.online.exam.service.impl;


import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.repo.AnswerRepo;

import com.online.exam.repo.QuestionRepo;
import com.online.exam.service.QuestionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QueryHelper queryHelper;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private AnswerRepo answerRepo;
    @Override
    public Question createQuestion(Long userId, Long courseId, Question question) throws Exception {
        Question resultQuestion = new Question();
        User retrievedUser = this.queryHelper.getUserMethod(userId);
        Course retrievedCourse = this.queryHelper.getCourseMethod(courseId);
        Set<Course> courses=retrievedUser.getCourses();
        if(!courses.isEmpty()) {
            outerLoop:
            for (Course eachCourse : courses
            ) {
                if (eachCourse.getCourseId().equals(retrievedCourse.getCourseId())) {


                    Answer answer = new Answer();
                    if (question.getAnswerChoice().equals(AnswerChoice.choice1)) {
                        answer.setAnswerStatus("active");
                        answer.setAnswerCreated(new Date());
                        answer.setAnswerContent(question.getChoice1());

                        answer = answerRepo.save(answer);

                        question.setAnswer(answer);
                        question.setAnswerChoice(AnswerChoice.choice1);


                    } else if (question.getAnswerChoice().equals(AnswerChoice.choice2)) {
                        answer.setAnswerStatus("active");
                        answer.setAnswerCreated(new Date());
                        answer.setAnswerContent(question.getChoice2());
                        answer = this.answerRepo.save(answer);
                        question.setAnswer(answer);
                        question.setAnswerChoice(AnswerChoice.choice2);


                    } else if (question.getAnswerChoice().equals(AnswerChoice.choice3)) {
                        answer.setAnswerStatus("active");
                        answer.setAnswerCreated(new Date());
                        answer.setAnswerContent(question.getChoice3());
                        answer = this.answerRepo.save(answer);
                        question.setAnswer(answer);
                        question.setAnswerChoice(AnswerChoice.choice3);


                    } else if (question.getAnswerChoice().equals(AnswerChoice.choice4)) {
                        answer.setAnswerStatus("active");
                        answer.setAnswerCreated(new Date());
                        answer.setAnswerContent(question.getChoice4());
                        answer = this.answerRepo.save(answer);
                        question.setAnswer(answer);
                        question.setAnswerChoice(AnswerChoice.choice4);

                    } else {
                        throw new Exception("Please select the valid answer choice!!!");
                    }

                    question.setCourse(retrievedCourse);
                    question.setUser(retrievedUser);

                    resultQuestion = this.questionRepo.save(question);
                    break outerLoop;
                }
            }
        }


        return resultQuestion;
    }


    @Override
    public Question updateQuestion(Long userId,Long qusId, Question question) throws Exception {
        User retrievedUser = this.queryHelper.getUserMethod(userId);
        Question retrievedQuestion = this.queryHelper.getQuestionMethod(qusId);
        Question resultQuestion = new Question();
        List<Question> questions = retrievedUser.getQuestions();
        if (!questions.isEmpty()) {
            OuterLoop:for (Question eachQuestion : questions
            ) {
                if (eachQuestion.getQuestionId().equals(resultQuestion.getQuestionId())) {

                    if (!question.getQuestionTitle().isEmpty()) {
                        retrievedQuestion.setQuestionTitle(question.getQuestionTitle());
                    }
                    if (question.getQuestionMarks() >= 0) {
                        retrievedQuestion.setQuestionMarks(question.getQuestionMarks());
                    }

                    AnswerChoice updatedAnswer = question.getAnswerChoice();
                    if (!updatedAnswer.equals(retrievedQuestion.getAnswerChoice())) {
                        Answer answer = retrievedQuestion.getAnswer();
                        if (question.getAnswerChoice().equals(AnswerChoice.choice1)) {
                            answer.setAnswerStatus("active");
                            answer.setAnswerCreated(new Date());
                            answer.setAnswerContent(question.getChoice1());

                            answer = answerRepo.save(answer);

                            retrievedQuestion.setAnswer(answer);
                            retrievedQuestion.setAnswerChoice(AnswerChoice.choice1);


                        } else if (question.getAnswerChoice().equals(AnswerChoice.choice2)) {
                            answer.setAnswerStatus("active");
                            answer.setAnswerCreated(new Date());
                            answer.setAnswerContent(question.getChoice2());
                            answer = this.answerRepo.save(answer);
                            retrievedQuestion.setAnswer(answer);
                            retrievedQuestion.setAnswerChoice(AnswerChoice.choice3);


                        } else if (question.getAnswerChoice().equals(AnswerChoice.choice3)) {
                            answer.setAnswerStatus("active");
                            answer.setAnswerCreated(new Date());
                            answer.setAnswerContent(question.getChoice3());
                            answer = this.answerRepo.save(answer);
                            retrievedQuestion.setAnswer(answer);
                            retrievedQuestion.setAnswerChoice(AnswerChoice.choice3);


                        } else if (question.getAnswerChoice().equals(AnswerChoice.choice4)) {
                            answer.setAnswerStatus("active");
                            answer.setAnswerCreated(new Date());
                            answer.setAnswerContent(question.getChoice4());
                            answer = this.answerRepo.save(answer);
                            retrievedQuestion.setAnswer(answer);
                            retrievedQuestion.setAnswerChoice(AnswerChoice.choice4);
                        } else {
                            throw new Exception("Please select the valid answer choice!!!");
                        }
                    }
                    resultQuestion = this.questionRepo.save(retrievedQuestion);

                    break OuterLoop;

                }
            }


            }else{
            throw new Exception("there is no questions for the given user with user id " +retrievedUser.getUserId());
        }

        return resultQuestion;
    }

    @Override
    public List<Question> getQuestionByCourse(Long userId, Long courseId)  {
        List<Question> questions = new ArrayList<>();
        User retrievedUser = this.queryHelper.getUserMethod(userId);
        Course retrievedCourse = this.queryHelper.getCourseMethod(courseId);
        Set<Course> courses = retrievedUser.getCourses();
        if (!courses.isEmpty()) {
            for (Course eachCourse : courses
            ) {
                if (eachCourse.getCourseId().equals(courseId)) {
                    questions = this.questionRepo.findByCourse(retrievedCourse);
                }

            }


        }

        return questions;
    }

    @Override
    public String deleteQuestion(Long userId, Long qusId) throws Exception {

        String resultMessage = "";
        User retrievedUser = this.queryHelper.getUserMethod(userId);

        Question retrievedQuestion = this.queryHelper.getQuestionMethod(qusId);
        List<Question> questions = retrievedUser.getQuestions();
        if (!questions.isEmpty()) {
            OuterLoop:for (Question eachQuestion : questions
            ) {
                if (eachQuestion.getQuestionId().equals(retrievedQuestion.getQuestionId())) {
                    retrievedQuestion.setCourse(null);
                    Answer retrievedQuestionAnswer = retrievedQuestion.getAnswer();
                    retrievedQuestion.setAnswer(null);
                    this.questionRepo.save(retrievedQuestion);
                    this.answerRepo.delete(retrievedQuestionAnswer);
                    this.questionRepo.deleteQuestionByQuestionId(retrievedQuestion.getQuestionId());
                    resultMessage = "question deleted successfully for the id " + retrievedQuestion.getQuestionId();
                    break OuterLoop;

                }
            }


        } else {
            throw new Exception("there is no questions for the given user with id " + retrievedUser.getUserId());

        }
        return resultMessage;
    }


}
