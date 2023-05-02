package com.online.exam.repo;


import com.online.exam.model.Course;
import com.online.exam.model.Exam;
import com.online.exam.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamRepo extends JpaRepository<Exam,Long> {
    List<Exam> findByCourse(Course course);
    Exam findByExamTitle(String name);

}
