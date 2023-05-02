package com.online.exam.repo;

import com.online.exam.model.Course;
import com.online.exam.model.Exam;
import com.online.exam.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface QuestionRepo extends JpaRepository<Question,Long> {
    List<Question> findByCourse(Course course);
    Question findByQuestionTitle(String name);


    @Modifying
    @Query("delete from Question  q where q.questionId = ?1")
    void deleteQuestionByQuestionId(Long id);
    @Modifying
    @Query("select q from Question  q")
    Set<Question> findAllQuestionSet();
    @Query(value = "select q.questionId,q.questionTitle,q.answerChoice from  Question q where q.questionId not in(select q from q inner  join ExamQuestion eq on eq.question.questionId=q.questionId inner join Exam  e on e.examId=eq.exam.examId)",nativeQuery = true)
    List<Question> findByQuestionNotInExam();

}
