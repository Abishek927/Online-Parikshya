/*
package com.online.exam.service.impl;

import com.online.exam.helper.ExamHelper;
import com.online.exam.helper.HelperClass;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.repo.ExamRepo;
import com.online.exam.repo.RoleRepo;
import com.online.exam.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    private ExamRepo examRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private QueryHelper queryHelper;




     public Exam createExam(Long userId, Long facId, Long catId, Long courseId, Exam exam) throws Exception {
         User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty =this.queryHelper.getFacultyMethod(facId);
        Category retrievedCategory =this.queryHelper.getCategoryMethod(catId);
        Course retrievedCourse =this.queryHelper.getCourseMethod(courseId);
        List<userRole> userRoles=retrievedUser.getUserRoles();
        Faculty faculty=new Faculty();
        Exam resultExam=new Exam();






            for (userRole eachUserRole : userRoles
            ) {
                Role role = this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
                if (role.getRoleName().equalsIgnoreCase("teacher")) {
                    List<UserFaculty> userFaculties = retrievedUser.getUserFaculties();

                    for (UserFaculty eachUserFaculty : userFaculties
                    ) {
                        if (eachUserFaculty.getFaculty().getFacultyId().equals(retrievedFaculty.getFacultyId())) {
                            faculty = eachUserFaculty.getFaculty();
                        }


                        List<Category> categories = faculty.getCategoryList();
                        if (!categories.isEmpty()) {
                            for (Category eachCategory : categories
                            ) {
                                if (eachCategory.getCategoryId().equals(retrievedCategory.getCategoryId())) {
                                    List<Course> courses = eachCategory.getCourseList();
                                    if (!courses.isEmpty()) {
                                        for (Course eachCourse : courses
                                        ) {
                                            if (eachCourse.getCourseId().equals(retrievedCourse.getCourseId())) {
                                                List<Exam> exams = eachCourse.getExams();
                                                if (!exams.isEmpty()) {
                                                    for (Exam eachExam : exams
                                                    ) {
                                                        Exam exam1 = this.examRepo.findByExamTitle(eachExam.getExamTitle());
                                                        if (exam1.getExamTitle().equalsIgnoreCase(exam.getExamTitle())) {
                                                            throw new Exception("Exam with the given exam title " + exam.getExamTitle() + " already exists!!!");

                                                        } else {
                                                            ExamHelper examHelper=new ExamHelper();

                                                            if (examHelper.generateValidDate(exam.getExamStartedTime(),exam.getExamEndedTime())) {
                                                                Long totalTime=examHelper.generateTotalExamTime(exam.getExamStartedTime(),exam.getExamEndedTime());
                                                                if(totalTime!=null) {
                                                                    exam.setExamTimeLimit(totalTime);
                                                                }else {
                                                                    throw new Exception("something went wrong!!!");
                                                                }
                                                                HelperClass helperClass=new HelperClass();
                                                                List<Question> questions=examHelper.generateQuestion(helperClass.findAllQuestion(),exam.getExamQuestionDisplayLimit(),exam.getQuestionPattern());
                                                                if(!questions.isEmpty()){
                                                                    ArrayList<ExamQuestion> examQuestions=new ArrayList<ExamQuestion>();
                                                                    ExamQuestion question=new ExamQuestion();
                                                                    for (Question eachQuestion:questions
                                                                         ) {
                                                                        question.setQuestion(eachQuestion);
                                                                    }
                                                                    question.setExam(exam);
                                                                    examQuestions.add(question);
                                                                    exam.setExamquestions(examQuestions);





                                                                }
                                                                 else {
                                                                    throw new Exception("Please select the appropriate question pattern");
                                                                }
                                                                int totalMarks=examHelper.generateTotalMarks(helperClass.findAllQuestion(),exam.getExamQuestionDisplayLimit(),exam.getQuestionPattern());
                                                                exam.setExamTotalMarks(totalMarks);


                                                                exam.setCourse(retrievedCourse);
                                                                resultExam = this.examRepo.save(exam);


                                                            } else {
                                                                throw new Exception("Invalid time!!!");
                                                            }


                                                        }
                                                    }
                                                }
                                            }
                                        }


                                    } else {
                                        throw new Exception("there is no courses for the given category with id " + eachCategory.getCategoryId());
                                    }
                                }


                            }
                        } else {
                            throw new Exception("there is no categories for the given faculty with id " + retrievedFaculty.getFacultyId());
                        }
                    }


                }


            }



        return resultExam;
    }

    @Override
    public String deleteExam(Long userId,Long facId, Long catId, Long courseId, Long examId) throws Exception {
        String message="";

        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty =this.queryHelper.getFacultyMethod(facId);
        Category retrievedCategory =this.queryHelper.getCategoryMethod(catId);
        Course retrievedCourse =this.queryHelper.getCourseMethod(courseId);
        Exam exam=this.queryHelper.getExamMethod(examId);
        Faculty faculty=new Faculty();

        List<userRole> userRoles=retrievedUser.getUserRoles();
        for (userRole eachUserRole:userRoles
             ) {
            Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
            if(role.getRoleName().equalsIgnoreCase("teacher")){
                List<UserFaculty> userFaculties=retrievedUser.getUserFaculties();
                for (UserFaculty eachUserFaculty:userFaculties
                     ) {
                    if(eachUserFaculty.getFaculty().getFacultyId().equals(retrievedFaculty.getFacultyId())){
                        faculty=eachUserFaculty.getFaculty();
                    }
                }

            }

            }

            List<Category> categories=faculty.getCategoryList();
            if(!categories.isEmpty()) {
                for (Category eachCategory : categories
                ) {
                    if (eachCategory.getCategoryId().equals(retrievedCategory.getCategoryId())){
                        List<Course> courses=eachCategory.getCourseList();
                        if(!courses.isEmpty()){
                            for (Course eachCourse:courses
                                 ) {
                                if(eachCourse.getCourseId().equals(retrievedCourse.getCourseId())){
                                    List<Exam> exams=eachCourse.getExams();
                                    for (Exam eachExam:exams
                                         ) {
                                        if(eachExam.getExamId().equals(exam.getExamId())){
                                            List<ExamQuestion> examQuestions=eachExam.getExamquestions();
                                            if(!examQuestions.isEmpty()){
                                                for (ExamQuestion eachExamQuestion:examQuestions
                                                     ) {
                                                    if(eachExamQuestion.getExam().getExamId().equals(eachExam.getExamId())){
                                                        eachExamQuestion.setExam(null);
                                                        eachExamQuestion.setQuestion(null);
                                                    }
                                                }
                                            }
                                            exam.setCourse(null);
                                            exam.setExamStatus(false);

                                            this.examRepo.deleteById(examId);
                                            return message="exam deleted successfully!!!";
                                        }
                                    }
                                }
                                else{
                                    return message="exam with the given id "+examId+" does not exist in the given course with id "+courseId;
                                }

                            }

                        }else {
                            throw new Exception("No courses for the given category with catId "+eachCategory.getCategoryId());
                        }
                    }

                }
            }else {
                throw new Exception("No categories for the given faculty");
            }





        return null;
    }

    @Override
    public Exam updateExam(Long userId,Long facId, Long catId, Long courseId, Long examId, Exam exam) throws Exception {
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty =this.queryHelper.getFacultyMethod(facId);
        Category retrievedCategory =this.queryHelper.getCategoryMethod(catId);
        Course retrievedCourse =this.queryHelper.getCourseMethod(courseId);
        Exam retrievedExam=this.queryHelper.getExamMethod(examId);
        List<userRole> userRoles=retrievedUser.getUserRoles();
        Exam updatedExam=new Exam();
        for (userRole eachUserRole:userRoles
             ) {
            Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
            if(role.getRoleName().equalsIgnoreCase("teacher")){
                List<UserFaculty> userFaculties=retrievedUser.getUserFaculties();
                for (UserFaculty eachUserFaculty:userFaculties
                ) {
                    if(eachUserFaculty.getFaculty().getFacultyId().equals(retrievedFaculty)){
                        List<Category> categories=eachUserFaculty.getFaculty().getCategoryList();
                        if(!categories.isEmpty()){
                            for (Category eachCategory:categories
                            ) {
                                if(eachCategory.getCategoryId().equals(retrievedCategory.getCategoryId())){
                                    List<Course> courses=eachCategory.getCourseList();
                                    if(!courses.isEmpty()){
                                        for (Course eachCourse:courses
                                             ) {
                                            if(eachCourse.getCourseId().equals(retrievedCourse.getCourseId())){
                                                List<Exam> exams=eachCourse.getExams();
                                                if(!exams.isEmpty()){
                                                    for (Exam eachExam:exams
                                                         ) {
                                                        if(eachExam.getExamId().equals(retrievedExam.getExamId())){
                                                         retrievedExam.setExamTitle(exam.getExamTitle());
                                                         retrievedExam.setExamTotalMarks(exam.getExamTotalMarks());
                                                         retrievedExam.setExamDesc(exam.getExamDesc());
                                                         Course updatedCourse=exam.getCourse();
                                                         if(updatedCourse!=null){
                                                         if(eachCategory.getCategoryId().equals(updatedCourse.getCourseId())){
                                                             retrievedExam.setCourse(updatedCourse);
                                                         }
                                                        }
                                                         Boolean updatedStatus=exam.getExamStatus();
                                                         if(updatedStatus==false){
                                                             retrievedExam.setExamStatus(updatedStatus);
                                                         }else{
                                                             retrievedExam.setExamStatus(Boolean.TRUE);
                                                         }
                                                         ExamHelper examHelper=new ExamHelper();
                                                         if(examHelper.generateValidDate(exam.getExamStartedTime(),exam.getExamEndedTime())) {

                                                             retrievedExam.setExamStartedTime(exam.getExamStartedTime());
                                                             retrievedExam.setExamEndedTime(exam.getExamEndedTime());

                                                         }
                                                         Long examTime=examHelper.generateTotalExamTime(exam.getExamStartedTime(),exam.getExamEndedTime());
                                                         if(examTime!=null){
                                                             retrievedExam.setExamTimeLimit(examTime);
                                                         }else{
                                                             throw new Exception("Something went wrong!!!");
                                                         }
                                                         HelperClass helperClass=new HelperClass();
                                                         List<Question> questions=examHelper.generateQuestion(helperClass.findAllQuestion(),exam.getExamQuestionDisplayLimit(),exam.getQuestionPattern());
                                                          if(!questions.isEmpty()){
                                                             List<ExamQuestion> examQuestions= exam.getExamquestions();
                                                             if(!examQuestions.isEmpty()){
                                                                 for (ExamQuestion eachExamQuestion:examQuestions
                                                                      ) {
                                                                     if(eachExamQuestion.getExam().getExamId().equals(eachExam.getExamId())){
                                                                         for (Question eachQuestion:questions
                                                                              ) {
                                                                             eachExamQuestion.setQuestion(eachQuestion);

                                                                         }
                                                                         eachExamQuestion.setExam(eachExamQuestion.getExam());
                                                                     }

                                                                 }
                                                                 retrievedExam.setExamquestions(examQuestions);
                                                             }
                                                             else{
                                                                 throw new Exception("there is no examquestions for the given exam with id "+retrievedExam.getExamId());
                                                             }

                                                              int totalMarks=examHelper.generateTotalMarks(questions,exam.getExamQuestionDisplayLimit(),exam.getQuestionPattern());

                                                              retrievedExam.setExamTotalMarks(totalMarks);
                                                          }else {
                                                              throw new Exception("Please select the appropriate question pattern");
                                                          }
                                                          updatedExam=this.examRepo.save(retrievedExam);



                                                        }

                                                    }

                                                }else {
                                                    throw new Exception("there is no exams for the given course with id "+eachCourse.getCourseId());
                                                }
                                            }
                                        }

                                    }else {
                                        throw new Exception("there is no courses for the given category with id "+eachCategory.getCategoryId());
                                    }
                                }

                            }

                        }else {
                            throw new Exception("there is no categories for the given faculty with id "+eachUserFaculty.getFaculty().getFacultyId());
                        }
                    }

                }


            }
        }






        return retrievedExam;
    }

    @Override
    public List<Exam> getExamByCourse(Long userId,Long facId, Long catId, Long courseId) throws Exception {
        List<Exam> resultExam=new ArrayList<>();
        User retrieveduser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty=this.queryHelper.getFacultyMethod(facId);
        Category retrievedCategory=this.queryHelper.getCategoryMethod(catId);
        Course retrievedCourse=this.queryHelper.getCourseMethod(courseId);
        Faculty faculty=new Faculty();
        List<Exam> exams=retrievedCourse.getExams();
        if(exams==null){
            return resultExam;
        }else {
            List<userRole> userRoles=retrieveduser.getUserRoles();
            if(!userRoles.isEmpty()){
                for (userRole eachUserRole:userRoles
                     ) {
                    Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
                    if(role.getRoleName().equalsIgnoreCase("student")||role.getRoleName().equalsIgnoreCase("teacher")){
                        List<UserFaculty> userFaculties=retrieveduser.getUserFaculties();
                        for (UserFaculty eachUserFaculty:userFaculties
                             ) {
                            if(eachUserFaculty.getFaculty().getFacultyId().equals(retrievedFaculty.getFacultyId())){
                                faculty=eachUserFaculty.getFaculty();
                            }
                        }
                    } else if (role.getRoleName().equalsIgnoreCase("admin")) {
                        faculty=retrievedFaculty;

                    }
                    else {
                        throw new Exception("Invalid role for the given user!!!");
                    }
                    List<Category> categories=faculty.getCategoryList();
                    if(!categories.isEmpty()){
                        for (Category eachCategory:categories
                             ) {
                            if(eachCategory.getCategoryId().equals(retrievedCategory.getCategoryId())){
                                List<Course> courses=eachCategory.getCourseList();
                                for (Course eachCourse:courses
                                     ) {
                                    if(eachCourse.getCourseId().equals(retrievedCourse.getCourseId())){
                                        resultExam=this.examRepo.findByCourse(eachCourse);
                                    }
                                }
                            }
                        }
                    }else {
                        throw new Exception("there is no categories for the given faculty with id "+retrievedFaculty);
                    }
                }
            }
            retrieveduser.getUserFaculties();
        }


        return resultExam;
    }

}
*/
