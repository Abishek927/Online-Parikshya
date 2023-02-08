package com.online.exam.service.impl;

import com.online.exam.dto.CategoryDto;
import com.online.exam.dto.CourseDto;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.repo.CourseRepo;
import com.online.exam.repo.RoleRepo;
import com.online.exam.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private QueryHelper queryHelper;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CourseDto createCourse(Long userId, Long facultyId, Long categoryId, CourseDto courseDto) throws Exception {


        Course resultCourse=null;
        User user=this.queryHelper.getUserMethod(userId);
        Faculty faculty =this.queryHelper.getFacultyMethod(facultyId);
        Category category =this.queryHelper.getCategoryMethod(categoryId);

                List<Course> courses = category.getCourseList();
                if (!courses.isEmpty()) {
                    for (Course eachCourse : courses
                    ) {
                        Course retrievedCourse = this.courseRepo.findByCourseTitle(eachCourse.getCourseTitle());
                        if (retrievedCourse.getCourseTitle().equals(courseDto.getCourseName())) {
                            throw new Exception("Course with the given title already exist in the given category");
                        }

                    }
                }




                    List<Category> categories = faculty.getCategoryList();
                    for (Category eachCategory : categories
                    ) {
                        if (eachCategory.getCategoryId().equals(category.getCategoryId())) {
                            Course course=this.modelMapper.map(courseDto,Course.class);
                            course.setCategory(category);
                            course.setUser(user);
                            course.setCourseCreated(new Date());
                            resultCourse = this.courseRepo.save(course);

                        }
                    }

                    return this.modelMapper.map(resultCourse,CourseDto.class);
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

                            Set<Faculty> faculties=user.getFaculties();
                            if(!faculties.isEmpty()) {
                                for (Faculty eachUserFaculties : faculties
                                ) {
                                    if (eachUserFaculties.getFacultyId().equals(faculty.getFacultyId())) {
                                        Faculty faculty1 = eachUserFaculties;
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

                    }



            }
        }




        return message;
    }

    @Override
    public CourseDto updateCourse(Long userId, Long facultyId, Long categoryId, Long courseId,CourseDto courseDto) throws Exception {
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty=this.queryHelper.getFacultyMethod(facultyId);
        Category retrievedCategory=this.queryHelper.getCategoryMethod(categoryId);
        Course retrievedCourse=this.queryHelper.getCourseMethod(courseId);



                        Set<Faculty> userFaculties = retrievedUser.getFaculties();
                        for (Faculty eachUserFaculty : userFaculties
                        ) {
                            Faculty faculty = eachUserFaculty;
                            if (faculty.getFacultyId().equals(retrievedFaculty.getFacultyId())) {
                                List<Category> categories = faculty.getCategoryList();
                                for (Category eachCategory : categories
                                ) {
                                    if (eachCategory.getCategoryId().equals(retrievedCategory.getCategoryId())) {
                                        List<Course> courses = eachCategory.getCourseList();
                                        for (Course eachCourse : courses
                                        ) {
                                            if (eachCourse.getCourseId().equals(retrievedCourse.getCourseId())) {
                                                retrievedCourse.setCourseTitle(courseDto.getCourseName());
                                                retrievedCourse.setCourseDesc(courseDto.getCourseDesc());
                                                CategoryDto updateCat= courseDto.getCategoryDto();
                                                if(updateCat!=null) {
                                                    if (updateCat.getCategoryId().equals(eachCategory.getCategoryId())) {


                                                        retrievedCourse.setCategory(this.modelMapper.map(courseDto.getCategoryDto(),Category.class));
                                                    }
                                                }else {
                                                    retrievedCourse.setCategory(eachCategory);
                                                }
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







        return this.modelMapper.map(retrievedCourse,CourseDto.class);
    }

    @Override
    public List<CourseDto> getCoursesByCategory(Long userId, Long facultyId, Long categoryId) throws Exception {
        List<Course> retrievedCourse=new ArrayList<>();
        User user=this.queryHelper.getUserMethod(userId);
       Faculty faculty=this.queryHelper.getFacultyMethod(facultyId);
       Category category=this.queryHelper.getCategoryMethod(categoryId);
       List<Course> courses=category.getCourseList();
       if(!courses.isEmpty()){
           List<userRole> userRoles=user.getUserRoles();
           for (userRole eachUserRole:userRoles
                ) {
               Role role=this.roleRepo.findByRoleName(eachUserRole.getRole().getRoleName());
               if(role.getRoleName().equalsIgnoreCase("admin")||role.getRoleName().equalsIgnoreCase("teacher")||role.getRoleName().equalsIgnoreCase("student")){
                   Set<Faculty> userFaculties=user.getFaculties();
                   if(!userFaculties.isEmpty()){
                       for (Faculty eachUserFaculty:userFaculties
                            ) {
                           if(eachUserFaculty.getFacultyId().equals(faculty.getFacultyId())){
                               Faculty faculty1=eachUserFaculty;
                               List<Category> categories=faculty1.getCategoryList();

                               for (Category eachCategory:categories
                                    ) {
                                   if(eachCategory.getCategoryId().equals(categoryId)){
                                       retrievedCourse=this.courseRepo.findByCategory(category);
                                   }
                               }
                           }
                           
                       }

                   }else{
                       throw new Exception("there is empty userfaculties for the given user!!!");
                   }
               }


               }

           }


        return retrievedCourse;
    }


}
