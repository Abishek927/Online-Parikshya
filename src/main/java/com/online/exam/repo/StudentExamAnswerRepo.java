package com.online.exam.repo;

import com.online.exam.model.StudentExamAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentExamAnswerRepo extends JpaRepository<StudentExamAnswer,Long> {
    @Query(value = "select sea.sea_id,sea.course_id_fk,sea.exam_id_fk,sea.user_id_fk from student_exam_aswer_table sea inner join" +
            " course_table c on sea.course_id_fk" +
            "=c.course_id inner join exam_table e on e.exam_id=sea.exam_id_fk inner join" +
            " user_table u on u.user_id=sea.user_id_fk where sea.exam_id_fk=?1 and sea.user_id_fk=?2",nativeQuery = true)
    StudentExamAnswer findStudentExamAnswerByUserAndExam(Long examId,Long userId);
}
