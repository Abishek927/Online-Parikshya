package com.online.exam.repo;

import com.online.exam.model.StudentExamAnswer;
import jakarta.persistence.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository

public interface StudentExamAnswerRepo extends JpaRepository<StudentExamAnswer,Long> {
    @Query(value = "select sea.sea_id,sea.course_id_fk,sea.exam_id_fk,sea.user_id_fk from student_exam_answer sea inner join" +
            " course_table c on sea.course_id_fk" +
            "=c.course_id inner join exam_table e on e.exam_id=sea.exam_id_fk inner join" +
            " user_table u on u.user_id=sea.user_id_fk where sea.exam_id_fk=?1 and sea.user_id_fk=?2",nativeQuery = true)
    StudentExamAnswer findStudentExamAnswerByUserAndExam(Long examId,Long userId);
    @Modifying
    @Query(value = "insert into student_exam_answer(course_id_fk,exam_id_fk,user_id_fk) values(?1,?2,?3)",nativeQuery = true)
    Integer insert(Long courseId,Long examId,Long userId);
}
