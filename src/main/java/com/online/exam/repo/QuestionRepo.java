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
    @Query(value = "select q.question_id,q.question_title,q.question_ch_1,q.question_ch_2,q.question_ch_3,q.question_ch_4,q.question_marks,q.answer_choice,q.selected,q.course_id_fk,q.user_id_fk,q.answer_id_fk from question_table q inner join exam_question_table eq on eq.question_id_fk=q.question_id inner join exam_table e on e.exam_id=eq.exam_id_fk where e.exam_id=?1",nativeQuery = true)
  List<Question> findQuestionByExam(Long examId);


    @Modifying
    @Query("delete from Question  q where q.questionId = ?1")
    void deleteQuestionByQuestionId(Long id);
    @Query(value = "select count(q.question_id) from question_table q where q.selected=?1",nativeQuery = true)
    Integer countQuestionBySelectedFlag(boolean flag);

    List<Question> findBySelected(Boolean flag);
    List<Question> findByUser(User user);
    @Modifying
    @Query("select q from Question  q")
    Set<Question> findAllQuestionSet();
    @Query(value = "select q.question_id,q.question_title,q.answer_choice, from  Question q where q.questionId not in(select q from q inner  join ExamQuestion eq on eq.question.questionId=q.questionId inner join Exam  e on e.examId=eq.exam.examId)",nativeQuery = true)
    List<Question> findByQuestionNotInExam();


    List<Question> findByDifficultyLevel(String difficultyLevel);

}
