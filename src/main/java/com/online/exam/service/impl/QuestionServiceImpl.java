/*
package com.online.exam.service.impl;

import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.repo.AnswerRepo;

import com.online.exam.repo.QuestionRepo;
import com.online.exam.repo.RoleRepo;
import com.online.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QueryHelper queryHelper;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AnswerRepo answerRepo;
    @Override
    public Question createQuestion(Long userId, Long facId, Long catId, Long courseId, Question question) throws Exception {
        Question resultQuestion=new Question();
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty=this.queryHelper.getFacultyMethod(facId);
        Category retrievedCategory =this.queryHelper.getCategoryMethod(catId);
        Course retrievedCourse=this.queryHelper.getCourseMethod(courseId);

        Question question1=this.questionRepo.findByQuestionTitle(question.getQuestionTitle());
        if(question1!=null){
            return resultQuestion;
        }else {
            List<userRole> userRoles =retrievedUser.getUserRoles();
            for (userRole eachUserRole:userRoles
                 ) {
                Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
                if(role.getRoleName().equalsIgnoreCase("teacher")){
                    List<UserFaculty> userFaculties=retrievedUser.getUserFaculties();
                    for (UserFaculty eachUserFaculty:userFaculties
                         ) {
                        if(eachUserFaculty.getFaculty().getFacultyId().equals(retrievedFaculty.getFacultyId())){
                            Faculty faculty=eachUserFaculty.getFaculty();
                            List<Category> categories=faculty.getCategoryList();
                            if(!categories.isEmpty()){
                                for (Category eachCategory:categories
                                     ) {
                                    if(eachCategory.getCategoryId().equals(retrievedCategory.getCategoryId())){
                                        List<Course> courses=eachCategory.getCourseList();
                                        if(!courses.isEmpty()){
                                            for (Course eachCourse:courses
                                                 ) {
                                                if(eachCourse.getCourseId().equals(retrievedCourse.getCourseId())){
                                                    List<Question> questions=eachCourse.getQuestions();
                                                    if(!questions.isEmpty()){
                                                        for (Question eachQuestion:questions
                                                             ) {
                                                            Question byQuestionTitle=this.questionRepo.findByQuestionTitle(eachQuestion.getQuestionTitle());
                                                            if(byQuestionTitle.getQuestionTitle().equalsIgnoreCase(question.getQuestionTitle())){
                                                                throw new Exception("question with the given title already exists!!!");
                                                            }else{

                                                               if(question.getAnswerChoice().equals(AnswerChoice.choice1)){
                                                                   question.getAnswer().setAnswerContent(question.getChoice1());

                                                               } else if (question.getAnswerChoice().equals(AnswerChoice.choice2)) {
                                                                   question.getAnswer().setAnswerContent(question.getChoice2());

                                                               } else if (question.getAnswerChoice().equals(AnswerChoice.choice3)) {
                                                                   question.getAnswer().setAnswerContent(question.getChoice3());

                                                               }
                                                               else {
                                                                   question.getAnswer().setAnswerContent(question.getChoice4());
                                                               }
                                                               question.getAnswer().setAnswerStatus("active");
                                                               question.getAnswer().setAnswerCreated(new Date());

                                                                resultQuestion=this.questionRepo.save(question);
                                                            }
                                                        }


                                                    }else {
                                                        throw new Exception("there is no questions for the given course with id "+eachCourse.getCourseId());
                                                    }
                                                }
                                            }
                                        }else {
                                            throw new Exception("there is no courses for the given category with id "+eachCategory.getCategoryId());
                                        }
                                    }
                                }

                            }else {
                                throw new Exception("there is no categories for the given faculty with id "+faculty.getFacultyId());
                            }
                        }

                    }
                }
            }
        }


        return resultQuestion;
    }



    @Override
    public Question updateQuestion(Long userId, Long facId, Long catId, Long courseId, Long qusId, Question question) throws Exception {
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty=this.queryHelper.getFacultyMethod(facId);
        Category retrievedCategory=this.queryHelper.getCategoryMethod(catId);
        Course retrievedCourse=this.queryHelper.getCourseMethod(courseId);
        Question retrievedQuestion=this.queryHelper.getQuestionMethod(qusId);
        Question resultQuestion=new Question();

        List<userRole>userRoles=retrievedUser.getUserRoles();
        for (userRole eachUserRole:userRoles
             ) {
                Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
                if(role.getRoleName().equalsIgnoreCase("teacher")){
                    List<UserFaculty> userFaculties=retrievedUser.getUserFaculties();
                    for (UserFaculty eachUserFaculty:userFaculties
                         ) {
                        if(eachUserFaculty.getFaculty().getFacultyId().equals(retrievedFaculty.getFacultyId())){
                            List<Category> categories =retrievedFaculty.getCategoryList();
                            if(!categories.isEmpty()){
                                for (Category eachCategory:categories
                                     ) {
                                    if(eachCategory.getCategoryId().equals(retrievedCategory.getCategoryId())){
                                        List<Course> courses=eachCategory.getCourseList();
                                        if(!courses.isEmpty()){
                                            for (Course eachCourse:courses
                                                 ) {
                                                if(eachCourse.getCourseId().equals(retrievedCourse.getCourseId())){
                                                    List<Question> questions=eachCourse.getQuestions();
                                                    if(!questions.isEmpty()){
                                                        for (Question eachQuestion:questions
                                                             ) {
                                                            if(eachQuestion.getQuestionId().equals(qusId)){
                                                                retrievedQuestion.setQuestionTitle(question.getQuestionTitle());
                                                                resultQuestion.setQuestionMarks(question.getQuestionMarks());
                                                                Answer updatedAnswer=question.getAnswer();
                                                                if(updatedAnswer!=null){


                                                                        resultQuestion.setAnswer(updatedAnswer);
                                                                }else {
                                                                    retrievedQuestion.setAnswer(retrievedQuestion.getAnswer());

                                                                }

                                                                resultQuestion=this.questionRepo.save(resultQuestion);



                                                            }else {
                                                                return resultQuestion;
                                                            }
                                                        }
                                                    }else {
                                                        throw new Exception("there is no questions for the given course with course id "+courseId);
                                                    }
                                                }
                                            }
                                        }else {
                                            throw new Exception("there is no courses for the given category with id "+eachCategory.getCategoryId());
                                        }


                                    }
                                }

                            }else{
                                throw new Exception("there is no categories for the given faculty with id "+retrievedFaculty.getFacultyId());
                            }
                        }


                    }

                }else {

                }
        }

        return resultQuestion;
    }

    @Override
    public List<Question> getQuestionByCourse(Long userId, Long facId, Long catId, Long courseId) throws Exception {
        List<Question> questions=new ArrayList<>();
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty=this.queryHelper.getFacultyMethod(facId);
        Category retrievedCategory=this.queryHelper.getCategoryMethod(catId);
        Course retrievedCourse=this.queryHelper.getCourseMethod(courseId);
        List<userRole> userRoles=retrievedUser.getUserRoles();
        for (userRole eachUserRole:userRoles
             ) {
            Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
            if(role.getRoleName().equalsIgnoreCase("teacher")){
                List<UserFaculty> userFaculties=retrievedUser.getUserFaculties();
                for (UserFaculty eachUserFaculty:userFaculties
                     ) {
                    if(eachUserFaculty.getFaculty().getFacultyId().equals(retrievedFaculty.getFacultyId())){
                        List<Category> categories=retrievedFaculty.getCategoryList();
                        if(!categories.isEmpty()){
                            for (Category eachCategory:categories
                                 ) {
                                if(eachCategory.getCategoryId().equals(retrievedCategory.getCategoryId())){
                                    List<Course> courses=retrievedCategory.getCourseList();
                                    if(!courses.isEmpty()){
                                        for (Course eachCourse:courses
                                             ) {
                                            if(eachCourse.getCourseId().equals(courseId)){
                                               questions =this.questionRepo.findByCourse(retrievedCourse);
                                            }

                                        }


                                    }else{
                                        throw new Exception("there is no courses for the given category with id "+eachCategory.getCategoryId());
                                    }
                                }

                            }
                        }else {
                            throw new Exception("there is no categories for the given faculty with id "+retrievedFaculty);
                        }
                    }
                }
            }


        }

        return questions;
    }

    @Override
    public String deleteQuestion(Long userId, Long facId, Long catId, Long courseId, Long qusId) throws Exception {
        String resultMessage="";
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty=this.queryHelper.getFacultyMethod(facId);
        Category retrievedCategory=this.queryHelper.getCategoryMethod(catId);
        Course retrievedCourse=this.queryHelper.getCourseMethod(courseId);
        Question retrievedQuestion=this.queryHelper.getQuestionMethod(qusId);

        List<userRole> userRoles=retrievedUser.getUserRoles();
        for (userRole eachUserRole:userRoles
             ) {
            Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
            if(role.getRoleName().equalsIgnoreCase("teacher")){
                List<UserFaculty> userFaculties=retrievedUser.getUserFaculties();
                for (UserFaculty eachUserFaculty:userFaculties
                     ) {
                    if(eachUserFaculty.getFaculty().getFacultyId().equals(retrievedFaculty.getFacultyId())){
                        List<Category> categories=retrievedFaculty.getCategoryList();
                        if(categories.isEmpty()){
                            for (Category eachCategory:categories
                                 ) {
                                if(eachCategory.getCategoryId().equals(retrievedCategory.getCategoryId())){
                                    List<Course> courses=eachCategory.getCourseList();
                                    if(!courses.isEmpty()){
                                        for (Course eachCourse:courses
                                             ) {
                                            if(eachCourse.getCourseId().equals(retrievedCourse.getCourseId())){
                                                List<Question> questions=eachCourse.getQuestions();
                                                if(!questions.isEmpty()){
                                                    for (Question eachQuestion:questions
                                                         ) {
                                                        if(eachQuestion.getQuestionId().equals(retrievedQuestion.getQuestionId())){
                                                            retrievedQuestion.setCourse(null);
                                                            Answer retrievedQuestionAnswer=retrievedQuestion.getAnswer();
                                                            this.answerRepo.deleteById(retrievedQuestion.getQuestionId());

                                                            this.questionRepo.deleteById(retrievedQuestion.getQuestionId());
                                                            resultMessage="question deleted successfully for the id "+retrievedQuestion.getQuestionId();
                                                            return resultMessage;
                                                        }
                                                    }
                                                    return resultMessage;

                                                }else {
                                                    throw new Exception("there is no questions for the given course with id "+retrievedCourse.getCourseId());

                                                }
                                            }
                                        }


                                    }else {
                                        throw new Exception("there is no courses for the given category with id "+retrievedCategory.getCategoryId());
                                    }
                                }


                            }


                        }else {
                            throw new Exception("there is no categories for the given faculty with id "+retrievedFaculty.getFacultyId());
                        }
                    }


                }
            }
        }

        return null;
    }


}
*/
