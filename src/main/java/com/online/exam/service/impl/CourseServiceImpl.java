package com.online.exam.service.impl;

import com.online.exam.dto.CategoryDto;
import com.online.exam.dto.CourseDto;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.repo.CourseRepo;

import com.online.exam.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public CourseDto createCourse(Long userId, Long facultyId, Long categoryId, CourseDto courseDto) throws Exception {


        Course resultCourse=new Course();
        User user=this.queryHelper.getUserMethod(userId);
        Faculty faculty =this.queryHelper.getFacultyMethod(facultyId);
        Category category =this.queryHelper.getCategoryMethod(categoryId);
        Set<Faculty> faculties=user.getFaculties();
        if(!faculties.isEmpty()) {
            for (Faculty eachFaculty:faculties
                 ) {
                if(eachFaculty.getFacultyId().equals(facultyId)){
                    List<Category> categories=eachFaculty.getCategoryList();
                    if(!categories.isEmpty()){
                        for (Category eachCategory:categories
                             ) {
                            if(eachCategory.getCategoryId().equals(categoryId)){
                                List<Course> courses = category.getCourseList();
                                if (!courses.isEmpty()) {
                                    for (Course eachCourse : courses
                                    ) {
                                        Course retrievedCourse = this.courseRepo.findByCourseTitle(eachCourse.getCourseTitle());
                                        if (retrievedCourse.getCourseTitle().equals(courseDto.getCourseTitle())) {
                                            throw new Exception("Course with the given title already exist in the given category");
                                        }

                                    }
                                }
                                    Course course=new Course();
                                course.setCourseTitle(courseDto.getCourseTitle());
                                course.setCourseDesc(courseDto.getCourseDesc());
                                course.setCourseCreated(new Date());
                                    CourseDto courseDto1=new CourseDto();
                                    course.setUsers(Set.of(user));
                                    user.getCourses().add(course);
                                    course.setCategory(category);
                                    resultCourse=this.courseRepo.save(course);
                                    courseDto1.setCourseId(resultCourse.getCourseId());
                                    courseDto1.setCourseTitle(resultCourse.getCourseTitle());
                                    courseDto1.setCourseDesc(resultCourse.getCourseDesc());
                                    courseDto1.setCategoryDto(this.modelMapper.map(resultCourse.getCategory(),CategoryDto.class));
                                    return courseDto1;


                                }
                            }

                    }else {
                        throw new Exception("there is no categories for given faculty with id "+facultyId);
                    }
                }
            }






        }else {
            throw new Exception("there is no faculty for the given user with id "+userId);
        }

                    return null;
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
                                                    course.setUsers(null);
                                                    user.getCourses().remove(course);
                                                    List<Exam> exams=course.getExams();
                                                    if(!exams.isEmpty()){
                                                        for (Exam eachExam:exams
                                                             ) {
                                                            eachExam.setCourse(null);

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
                        return this.modelMapper.map(retrievedCourse,CourseDto.class);
    }

    @Override
    public List<CourseDto> getCoursesByCategory(Long userId, Long facultyId, Long categoryId) throws Exception {
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
    public CourseDto getCourseById(Long userId, Long facultyId, Long categoryId, Long courseId) throws Exception {
        User retrievedUser=this.queryHelper.getUserMethod(userId);
        Faculty retrievedFaculty=this.queryHelper.getFacultyMethod(facultyId);
        Category retrievedCategory=this.queryHelper.getCategoryMethod(categoryId);
        Course retrievedCourse=this.queryHelper.getCourseMethod(courseId);
        CourseDto resultCourseDto=new CourseDto();

            Set<Faculty> faculties=retrievedUser.getFaculties();
            if(!faculties.isEmpty()){
                for (Faculty eachFaculty:faculties
                     ) {
                    if(eachFaculty.getFacultyId().equals(facultyId)){
                        List<Category> categories=eachFaculty.getCategoryList();
                    if(!categories.isEmpty()){
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


                        }


                    }else {
                        throw new Exception("there is no category for the given faculty with id "+facultyId);
                    }
                    }
                }

            }else {
                throw new Exception("there is no faculty for the given user with id "+userId);
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
