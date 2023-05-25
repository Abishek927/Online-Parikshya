package com.online.exam.repo;

import com.online.exam.model.Course;
import com.online.exam.model.Exam;
import com.online.exam.model.Question;
import com.online.exam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface QuestionRepo extends JpaRepository<Question,Long> {
    List<Question> findByCourse(Course course);
    @Query(value = "select q.questionId from Question q inner join ExamQuestion eq on eq.question.questionId=q.questionId inner join Exam  e on e.examId=eq.exam.examId where e.examId=?1",nativeQuery = true)
  List<Question> findQuestionByExam(Long examId);


    @Modifying
    @Query("delete from Question  q where q.questionId = ?1")
    void deleteQuestionByQuestionId(Long id);
    @Query(value = "select count(q.questionId) from Question q where q.selected=?1",nativeQuery = true)
    Integer countQuestionBySelectedFlag(boolean flag);

    List<Question> findBySelected(Boolean flag);
    List<Question> findByUser(User user);
    @Modifying
    @Query("select q from Question  q")
    Set<Question> findAllQuestionSet();
    @Query(value = "select q.questionId,q.questionTitle,q.answerChoice from  Question q where q.questionId not in(select q from q inner  join ExamQuestion eq on eq.question.questionId=q.questionId inner join Exam  e on e.examId=eq.exam.examId)",nativeQuery = true)
    List<Question> findByQuestionNotInExam();

}
