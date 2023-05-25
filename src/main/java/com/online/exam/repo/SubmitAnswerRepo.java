package com.online.exam.repo;

import com.online.exam.model.SubmitAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmitAnswerRepo extends JpaRepository<SubmitAnswer,Long> {
}
