package com.online.exam.repo;

import com.online.exam.model.StudentExamAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentExamAnswerRepo extends JpaRepository<StudentExamAnswer,Long> {
}
