package com.online.exam.repo;

import com.online.exam.model.SubmitAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmitAnswerRepo extends JpaRepository<SubmitAnswer,Long> {
    @Modifying
    @Query(value = "insert into submit_answer_table(answer_content,question_id,sea_id_fk)values(?1,?2,?3)",nativeQuery = true)
    Integer insert(String content,Long questionId,Long seaId);
}
