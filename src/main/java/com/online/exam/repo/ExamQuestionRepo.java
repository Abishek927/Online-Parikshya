package com.online.exam.repo;

import com.online.exam.model.ExamQuestion;
import com.online.exam.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionRepo extends JpaRepository<ExamQuestion,Long> {

}
