package com.online.exam.service.impl;

import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.repo.CourseRepo;
import com.online.exam.repo.RoleRepo;
import com.online.exam.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private QueryHelper queryHelper;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private CourseRepo courseRepo;

    @Override
    public Course createCourse(Long userId, Long facultyId, Long categoryId, Course course) throws Exception {

        Course resultCourse=new Course();
        User user=this.queryHelper.getUserMethod(userId);
        Faculty faculty =this.queryHelper.getFacultyMethod(facultyId);
        Category category =this.queryHelper.getCategoryMethod(categoryId);
        List<userRole> userRoles=user.getUserRoles();
        for (userRole eachUserRole:userRoles
             ) {
            Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
            if(role.getRoleName().equalsIgnoreCase("teacher")||role.getRoleName().equalsIgnoreCase("admin")){
                List<Course> courses=category.getCourseList();
                if(!courses.isEmpty()) {
                    for (Course eachCourse : courses
                    ) {
                        Course retrievedCourse = this.courseRepo.findByCourseTitle(eachCourse.getCourseTitle());
                        if (retrievedCourse.getCourseTitle().equals(course.getCourseTitle())) {
                            throw new Exception("Course with the given title already exist in the given category");
                        }

                    }
                }
                    List<UserFaculty> userFaculties = user.getUserFaculties();
                if(!userFaculties.isEmpty()){
                    for (UserFaculty eachUserFaculty : userFaculties
                    ) {
                        if(eachUserFaculty.getFaculty().getFacultyId().equals(faculty.getFacultyId())) {
                            Faculty faculty1 = eachUserFaculty.getFaculty();

                                List<Category> categories = faculty1.getCategoryList();
                                for (Category eachCategory : categories
                                ) {
                                    if (eachCategory.getCategoryId().equals(category.getCategoryId())) {
                                        course.setCategory(category);
                                        course.setUser(user);
                                        course.setCourseCreated(new Date());
                                        resultCourse = this.courseRepo.save(course);

                                    }
                                }

                        }

                    }

                    }else {
                    throw new Exception("user faculties is null for the given user");
                }

            }else {
                throw new Exception("Invalid role for the given user!!!");
            }

        }




        return resultCourse;
    }

    @Override
    public String deleteCourse(Long userId, Long facultyId, Long categoryId, Long courseId) throws Exception {
        String message="";
        User user=this.queryHelper.getUserMethod(userId);
        Faculty faculty=this.queryHelper.getFacultyMethod(facultyId);
        Category  category=this.queryHelper.getCategoryMethod(categoryId);
        Course course=this.queryHelper.getCourseMethod(courseId);


        List<Course> courses=category.getCourseList();
        if(courses.isEmpty()){
            throw new Exception("There is no course for the given category id "+categoryId);
        }else {
            for (Course eachCourse:courses
                 ) {
                if(eachCourse.getCourseId().equals(courseId)){
                    List<userRole> userRoles=user.getUserRoles();
                    for (userRole eachUserRole:userRoles
                         ) {
                        Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
                        if(role.getRoleName().equalsIgnoreCase("teacher")||role.getRoleName().equals("admin")){
                            List<UserFaculty> faculties=user.getUserFaculties();
                            if(!faculties.isEmpty()) {
                                for (UserFaculty eachUserFaculties : faculties
                                ) {
                                    if (eachUserFaculties.getFaculty().getFacultyId().equals(faculty.getFacultyId())) {
                                        Faculty faculty1 = eachUserFaculties.getFaculty();
                                        List<Category> categories = faculty1.getCategoryList();
                                        if (!categories.isEmpty()) {
                                            for (Category eachCategory:categories
                                                 ) {
                                                if(eachCategory.getCategoryId().equals(category.getCategoryId())){
                                                    course.setCategory(null);
                                                    course.setUser(null);
                                                    List<Exam> exams=course.getExams();
                                                    if(!exams.isEmpty()){
                                                        for (Exam eachExam:exams
                                                             ) {
                                                            eachExam.setCourse(null);

                                                        }
                                                    }
                                                    List<User> users=course.getUserList();
                                                    if(!users.isEmpty()){
                                                        for (User eachUser:users
                                                             ) {
                                                            eachUser.setCourse(null);
                                                        }
                                                    }
                                                    this.courseRepo.deleteById(course.getCourseId());
                                                    message="course with the id "+courseId+" deleted successfully";

                                                }
                                            }

                                        }else {
                                            throw new Exception("no categories for the given faculty with id "+faculty1.getFacultyId());
                                        }

                                    }

                                }
                            }else {
                                throw new Exception("no userfaculties for the given user with id "+user.getUserId());
                            }
                        }else {
                            throw new Exception("Invalid role of the given user!!!");
                        }
                    }

                }else{
                    throw new Exception("Course with the given id "+courseId+" doesnot belong to the given category with id "+categoryId);
                }

            }
        }




        return message;
    }

    @Override
    public Course updateCourse(Long userId, Long facultyId, Long categoryId, Long courseId,Course course) throws Exception {
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty=this.queryHelper.getFacultyMethod(facultyId);
        Category retrievedCategory=this.queryHelper.getCategoryMethod(categoryId);
        Course retrievedCourse=this.queryHelper.getCourseMethod(courseId);
        List<userRole> userRoles=retrievedUser.getUserRoles();
        if(userRoles.isEmpty()){
            throw new Exception("Provided role for the given user does not exist!!!");

        }else {
            for (userRole eachUserRole : userRoles
            ) {
                Role role = this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());

                    if (role.getRoleName().equalsIgnoreCase("teacher") || role.getRoleName().equals("admin")) {
                        List<UserFaculty> userFaculties = retrievedUser.getUserFaculties();
                        for (UserFaculty eachUserFaculty : userFaculties
                        ) {
                            Faculty faculty = eachUserFaculty.getFaculty();
                            if (faculty.getFacultyId().equals(retrievedFaculty.getFacultyId())) {
                                List<Category> categories = faculty.getCategoryList();
                                for (Category eachCategory : categories
                                ) {
                                    if (eachCategory.getCategoryId().equals(retrievedCategory.getCategoryId())) {
                                        List<Course> courses = eachCategory.getCourseList();
                                        for (Course eachCourse : courses
                                        ) {
                                            if (eachCourse.getCourseId().equals(retrievedCourse.getCourseId())) {
                                                retrievedCourse.setCourseTitle(course.getCourseTitle());
                                                retrievedCourse.setCourseDesc(course.getCourseDesc());
                                                retrievedCourse.setCategory(course.getCategory());
                                                retrievedCourse=this.courseRepo.save(retrievedCourse);


                                            }
                                        }
                                    }else{
                                        throw new Exception("provided category does not match with the list of category inside given faculty");
                                    }

                                }
                            }
                            else {
                                throw new Exception("provided faculty does not match with the list of faculty created by user");
                            }

                        }
                    }else {
                        throw new Exception("Invalid role for the given user");
                    }


            }
        }


        return retrievedCourse;
    }

    @Override
    public List<Course> getCoursesByCategory(Long userId, Long facultyId, Long categoryId) {
        return null;
    }

    @Override
    public Course getCourseByName(Long userId, Long facultyId, Long categoryId, String name) {
        return null;
    }
}
