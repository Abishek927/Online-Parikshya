package com.online.exam.repo;

import com.online.exam.model.Category;
import com.online.exam.model.Course;
import com.online.exam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CourseRepo extends JpaRepository<Course,Long> {
    List<Course> findByCategory(Category category);
    List<Course> findByUser(User user);
    Course findByCourseTitle(String title);

}
