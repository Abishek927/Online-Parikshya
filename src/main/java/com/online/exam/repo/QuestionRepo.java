package com.online.exam.repo;

import com.online.exam.model.Course;
import com.online.exam.model.Exam;
import com.online.exam.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends JpaRepository<Question,Long> {
    List<Question> findByCourse(Course course);
    Question findByQuestionTitle(String name);

}
