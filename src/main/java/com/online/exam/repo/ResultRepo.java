package com.online.exam.repo;

import com.online.exam.model.Exam;
import com.online.exam.model.Result;
import com.online.exam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepo extends JpaRepository<Result,Long> {
    Result findByUserAndExam(User user, Exam exam);
}
