package com.online.exam.controller;


import com.online.exam.helper.ApiResponse;
import com.online.exam.model.Exam;
import com.online.exam.service.ExamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    private ExamService examService;

    @PostMapping("/user/{uId}/fac/{facId}/cat/{catId}/course/{cId}/create")
    ResponseEntity<?> createExamController(@PathVariable Long uId, @PathVariable Long facId, @PathVariable Long catId, @PathVariable Long cId, @RequestBody Exam exam) throws Exception {
        Exam resultExam=this.examService.createExam(uId, facId, catId, cId, exam);
        return new ResponseEntity<>(resultExam, HttpStatusCode.valueOf(200));

    }
    @DeleteMapping("/user/{uId}/fac/{facId}/cat/{catId}/course/{cId}/delete")
    ResponseEntity<?> deleteExamController(@PathVariable Long uId,@PathVariable Long facId,@PathVariable Long catId,@PathVariable Long cId,@RequestParam("examId")Long examId ) throws Exception {
        String message=this.examService.deleteExam(uId, facId, catId, cId, examId);
        if(message.contains("deleted")){
            return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(new ApiResponse(message,false),HttpStatusCode.valueOf(500));

    }

    @PutMapping("/user/{uId}/fac/{facId}/cat/{catId}/course/{cId}/update")
    ResponseEntity<?> updateExamController(@PathVariable Long uId,@PathVariable Long facId,@PathVariable Long catId,@PathVariable Long cId,@RequestParam("examId")Long examId,@RequestBody Exam exam) throws Exception {
                Exam updatedExam=this.examService.updateExam(uId, facId, catId, cId, examId, exam);
                if(updatedExam!=null){
                    return new ResponseEntity<>(updatedExam,HttpStatusCode.valueOf(200));
                }
                return new ResponseEntity<>(new ApiResponse("something went wrong",false),HttpStatusCode.valueOf(500));
    }

    @GetMapping("/user/{uId}/fac/{facId}/cat/{catId}/course/read")
    ResponseEntity<?> getExamByCourseController(@PathVariable Long uId,@PathVariable Long facId,@PathVariable Long catId,@RequestParam("courseId")Long courseId) throws Exception {
        List<Exam> exams =this.examService.getExamByCourse(uId, facId, catId, courseId);
        if(!exams.isEmpty()){
            return new ResponseEntity<>(exams,HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(new ApiResponse("Something went wrong!!!",false),HttpStatusCode.valueOf(500));
    }

}
