package com.online.exam.repo;


import com.online.exam.model.Course;
import com.online.exam.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExamRepo extends JpaRepository<Exam,Long> {
    @Transactional(readOnly = true)
    List<Exam> findByCourse(Course course);
    Exam findByExamTitle(String name);

}
