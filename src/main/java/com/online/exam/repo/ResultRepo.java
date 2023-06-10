package com.online.exam.repo;

import com.online.exam.model.Exam;
import com.online.exam.model.Result;
import com.online.exam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepo extends JpaRepository<Result,Long> {
    Result findByUserAndExam(User user, Exam exam);
    @Query(value = "select  c.course_title from user_table u inner join user_course uc on uc.user_id_fk=u.user_id inner join course_table c on c.course_id=uc.course_id_fk where u.user_id=?1",nativeQuery = true)
    String findCourseName(Long userId);

    List<Result> findByUser(User user);
}
