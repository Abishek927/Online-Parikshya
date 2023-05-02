package com.online.exam.service.impl;

import com.online.exam.dto.CategoryDto;
import com.online.exam.dto.CourseDto;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.repo.CourseRepo;

import com.online.exam.repo.UserRepo;
import com.online.exam.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private QueryHelper queryHelper;

    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepo userRepo;

    @Transactional
    @Override
    public CourseDto createCourse(CourseDto courseDto,Principal principal) throws Exception {
        User retrievedUser=queryHelper.getUserMethod(courseDto.getUserId());
        Category retrievedCategory=queryHelper.getCategoryMethod(courseDto.getCategoryId());
        User loggedInUser=userRepo.findByUserEmail(principal.getName());
        if(retrievedCategory.getUsers().contains(retrievedUser)&&loggedInUser.equals(retrievedUser)){
            List<Course> retrievedCourse=retrievedCategory.getCourseList();
           if(!retrievedCourse.isEmpty()) {

               for (Course eachCourse : retrievedCourse
               ) {
                   Course redundantCourse = this.courseRepo.findByCourseTitle(eachCourse.getCourseTitle());
                   if (redundantCourse != null) {
                       throw new Exception("Course with the given title already exist in the given category");
                   }
               }
           }
                        Course course = new Course();
                        course.setCourseTitle(courseDto.getCourseTitle());
                        course.setCourseDesc(courseDto.getCourseDesc());
                        course.setCourseCreated(new Date());
                        course.setUsers(Set.of(retrievedUser));
                        retrievedUser.getCourses().add(course);
                        course.setCategory(retrievedCategory);
                        this.courseRepo.save(course);
                        return courseDto;
        }
        return null;
    }

    @Transactional
    @Override
    public String deleteCourse(Long courseId, Principal principal) throws Exception {
        String message = "";
        User  user=userRepo.findByUserEmail(principal.getName());
        Course course=queryHelper.getCourseMethod(courseId);
        if(course.getUsers().contains(user))
        {
        course.setCategory(null);
        course.setUsers(null);
        user.getCourses().remove(course);
        List<Exam> exams = course.getExams();
        if (!exams.isEmpty()) {
            for (Exam eachExam : exams
            ) {
                eachExam.setCourse(null);

            }
        }
        this.courseRepo.deleteById(course.getCourseId());
        message = "course with the id " + courseId + " deleted successfully";
        return message;
    }

    @Override
    public CourseDto updateCourse(CourseDto,Principal principal) {
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
                                                if(!courseDto.getCourseTitle().isEmpty()) {
                                                    retrievedCourse.setCourseTitle(courseDto.getCourseTitle());
                                                }
                                                if(!courseDto.getCourseTitle().isEmpty()) {
                                                    retrievedCourse.setCourseDesc(courseDto.getCourseDesc());
                                                }
                                                CategoryDto updateCat= courseDto.getCategoryDto();
                                                if(updateCat!=null) {
                                                    List<Category> categories1=faculty.getCategoryList();
                                                    for (Category eachCat:categories1
                                                         ) {
                                                        if(eachCat.getCategoryId().equals(updateCat.getCategoryId())){
                                                            Category category=this.queryHelper.getCategoryMethod(courseDto.getCategoryDto().getCategoryId());
                                                            retrievedCourse.setCategory(category);
                                                        }




                                                    }
                                                }
                                                retrievedCourse=this.courseRepo.save(retrievedCourse);


                                            }
                                        }
                                    }

                                }
                            }
                            else {
                                throw new Exception("provided faculty does not match with the list of faculty created by user");
                            }

                        }
                        return cou;
    }

    @Override
    public List<CourseDto> getCoursesByCategory(Long categoryId) throws Exception {
        List<Course> retrievedCourse=new ArrayList<>();
        List<CourseDto> courseDtos=new ArrayList<>();
        User user=this.queryHelper.getUserMethod(userId);
       Faculty faculty=this.queryHelper.getFacultyMethod(facultyId);
       Category category=this.queryHelper.getCategoryMethod(categoryId);
       List<Course> courses=category.getCourseList();
       if(!courses.isEmpty()){

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
                                      courseDtos=retrievedCourse.stream().map(course -> this.modelMapper.map(course,CourseDto.class)).collect(Collectors.toList());
                                   }
                               }
                           }
                           
                       }

                   }else{
                       throw new Exception("there is empty userfaculties for the given user!!!");
                   }



               }else {
           return null;
       }




        return courseDtos;
    }

    @Override
    public CourseDto getCourseById(Long courseId) throws Exception {





                        for (Category eachCategory:categories
                             ) {
                            if(eachCategory.getCategoryId().equals(categoryId)){
                                List<Course> courses=eachCategory.getCourseList();
                                if(!courses.isEmpty()){
                                    for (Course eachCourse:courses
                                         ) {
                                        if(eachCourse.getCourseId().equals(retrievedCourse.getCourseId())){
                                            resultCourseDto.setCourseId(eachCourse.getCourseId());
                                            resultCourseDto.setCourseTitle(eachCourse.getCourseTitle());
                                            resultCourseDto.setCategoryDto(this.modelMapper.map(eachCourse.getCategory(),CategoryDto.class));
                                            resultCourseDto.setExams(eachCourse.getExams());
                                            return resultCourseDto;
                                        }
                                    }


                                }else {
                                    throw new Exception("there is no courses for the given category with id "+categoryId);
                                }
                            }




        return null;
    }

    @Override
    public List<CourseDto> getAllCourse(Long userId) {
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Set<Course> courses=retrievedUser.getCourses();
        List<CourseDto> courseDtos=new ArrayList<>();
        if(!courses.isEmpty()){
            for (Course eachCourse:courses
                 ) {
                CourseDto courseDto=new CourseDto();
                courseDto.setCourseId(eachCourse.getCourseId());
                courseDto.setCourseTitle(eachCourse.getCourseTitle());
                courseDto.setCourseDesc(eachCourse.getCourseDesc());
                courseDto.setCategoryDto(this.modelMapper.map(eachCourse.getCategory(),CategoryDto.class));
                courseDto.setExams(eachCourse.getExams());
                courseDtos.add(courseDto);
            }
            return courseDtos;

        }
        return null;
    }


}
