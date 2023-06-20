package com.online.exam.repo;

import com.online.exam.model.Question;
import com.online.exam.model.StudentExamAnswer;
import com.online.exam.model.SubmitAnswer;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmitAnswerRepo extends JpaRepository<SubmitAnswer,Long> {
    @Modifying
    @Query(value = "insert into submit_answer_table(answer_content,question_id,sea_id_fk,is_correct)values(?1,?2,?3,?4)",nativeQuery = true)
    Integer insert(String content,Long questionId,Long seaId,Boolean status);

    @Query(value = "select count(sat.id) from submit_answer_table sat where sat.is_correct=1 and sat.question_id=?1 ",nativeQuery = true)
    Integer countTotalCorrectAnswerForGivenQuestion(Long qusId);
    @Query(value = "select count(sat.id) from submit_answer_table sat where sat.is_correct=0 and sat.question_id=?1 ",nativeQuery = true)
    Integer countTotalFalseAnswerForGivenQuestion(Long qusId);

    List<SubmitAnswer> findByStudentExamAnswer(StudentExamAnswer studentExamAnswer);
}
