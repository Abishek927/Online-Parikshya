package com.online.exam.service.impl;

import com.online.exam.dto.CategoryDto;
import com.online.exam.dto.CourseDto;
import com.online.exam.helper.QueryHelper;
import com.online.exam.model.*;
import com.online.exam.repo.CourseRepo;

import com.online.exam.repo.UserRepo;
import com.online.exam.service.CourseService;
import com.online.exam.service.ExamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

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
    @Autowired
    private ExamService examService;

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
    public String deleteCourse(Long courseId, Principal principal){
        String message = "";
        User user = userRepo.findByUserEmail(principal.getName());
        Course course = queryHelper.getCourseMethod(courseId);
        if (course.getUsers().contains(user)) {
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
        return message;
    }

    @Transactional
        @Override
        public String updateCourse(CourseDto courseDto,Principal principal) throws Exception{
                Course retrievedCourse=queryHelper.getCourseMethod(courseDto.getCourseId());
                User loggedInUser=userRepo.findByUserEmail(principal.getName());
                if(loggedInUser.getCourses().contains(retrievedCourse)&&retrievedCourse.getUsers().contains(loggedInUser)){
                if (!courseDto.getCourseTitle().isEmpty()) {
                    retrievedCourse.setCourseTitle(courseDto.getCourseTitle());
                }
                if (!courseDto.getCourseDesc().isEmpty()) {
                    retrievedCourse.setCourseDesc(courseDto.getCourseDesc());
                }
                Long updateCatId =courseDto.getCategoryId();
                Category updatedCategory=queryHelper.getCategoryMethod(updateCatId);
                if(!updatedCategory.equals(retrievedCourse.getCategory())){
                    if(loggedInUser.getCategories().contains(updatedCategory)){
                        retrievedCourse.setCategory(updatedCategory);
                        }
                    }
                this.courseRepo.save(retrievedCourse);


            }
                return "Course updated successfully!!!";
        }
    @Override
    public List<CourseDto> getCoursesByCategory(Long categoryId) {
        List<CourseDto> courseDtos=new ArrayList<>();
        CourseDto courseDto=new CourseDto();
        List<Course> courses=courseRepo.findByCategory(queryHelper.getCategoryMethod(categoryId));
        if(courses.isEmpty()){
            return null;
        }else{
            for (Course eachCourse:courses
                 ) {
                courseDto.setCourseId(eachCourse.getCourseId());
                courseDto.setCourseTitle(eachCourse.getCourseTitle());
                courseDtos.add(courseDto);
            }
        }
        return courseDtos;
    }

    @Override
    public CourseDto getCourseById(Long courseId) throws Exception {
        return this.modelMapper.map(courseRepo.getReferenceById(courseId),CourseDto.class);
    }

    @Override
    public List<CourseDto> getAllCourse(Principal principal) {
        User retrievedUser=userRepo.findByUserEmail(principal.getName());
        List<Course> courses=courseRepo.findAll();
        courses=courses.stream().filter(course -> course.getUsers().contains(retrievedUser)).collect(Collectors.toList());
        return courses.stream().map(course -> this.modelMapper.map(course,CourseDto.class)).collect(Collectors.toList());
    }

    @Override
    public Integer countCourseByUser(Principal principal) {
        User loggedInUser=userRepo.findByUserEmail(principal.getName());
        Integer courseCount=(loggedInUser.getCourses().size()>0)?courseRepo.countCourseByUsers(Set.of(loggedInUser)):0;
        return courseCount;
    }

    @Override
    public Map<Integer,List<CourseDto>> getCoursesByCategoryForStudent(Principal principal) {
        Map<Integer,List<CourseDto>> message=new HashMap<>();
        List<CourseDto> courseDtos=new ArrayList<>();
        CourseDto courseDto=new CourseDto();
        User loggedInUser=userRepo.findByUserEmail(principal.getName());
        Set<Category> category=loggedInUser.getCategories();
        if(category.size()==1)
        {
            for (Category eachCategory:category
                 ) {
                List<Course> courses=eachCategory.getCourseList();
                if(courses.isEmpty()){
                    message.put(500,null);
                    return message;
                }
                for (Course eachCourse:courses
                     ) {
                    courseDto.setCourseId(eachCourse.getCourseId());
                    courseDto.setCourseTitle(eachCourse.getCourseTitle());
                    courseDto.setExams(examService.getExamByCourse(eachCourse.getCourseId()));
                    courseDtos.add(courseDto);

                }
            }
        }
        message.put(200,courseDtos);
        return message;
    }
}