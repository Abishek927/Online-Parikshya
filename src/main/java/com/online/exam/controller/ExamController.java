package com.online.exam.controller;

import com.online.exam.dto.ExamDto;
import com.online.exam.dto.SubmitAnswerDto;
import com.online.exam.helper.ApiResponse;
import com.online.exam.model.Exam;
import com.online.exam.model.User;
import com.online.exam.repo.UserRepo;
import com.online.exam.service.ExamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    private ExamService examService;


    @PostMapping("/create")
    @PreAuthorize("hasAuthority('create_exam')")
    ResponseEntity<?> createExamController(@RequestBody ExamDto examDto, Principal principal) throws Exception {
        Map<String,Object> message= this.examService.createExam(examDto,principal);
        return ResponseEntity.status(HttpStatus.OK).body(message);


    }
    @DeleteMapping("/delete/{examId}")
    @PreAuthorize("hasAuthority('delete_exam')")
    ResponseEntity<?> deleteExamController(@PathVariable("examId")Long examId,Principal principal) throws Exception {
        Map<Integer,String> message= this.examService.deleteExam(examId,principal);
            if (message.containsKey(200)) {
                return new ResponseEntity<>(message, HttpStatusCode.valueOf(200));
            }
            return new ResponseEntity<>(message, HttpStatusCode.valueOf(500));


    }

   @PutMapping("/update")
   @PreAuthorize("hasAuthority('create_exam')")
   ResponseEntity<?> updateExamController(@RequestBody ExamDto examDto,Principal principal) throws Exception {
                Map<Integer,String> message=this.examService.updateExam(examDto,principal);
                if(message.containsKey(200)){
                    return new ResponseEntity<>(message,HttpStatusCode.valueOf(200));
                }
                return new ResponseEntity<>(message,HttpStatusCode.valueOf(500));
    }

    @GetMapping("/read-exams/{courseId}")
    @PreAuthorize("hasAuthority('view_exam')")
    ResponseEntity<?> getExamByCourseController(@PathVariable("courseId")Long courseId) throws Exception {
        Map<String,Object> message=new HashMap<>();
        List<ExamDto> exams =this.examService.getExamByCourse(courseId);
        if(!exams.isEmpty()){
            message.put("status",200);
            message.put("data",exams);
            return new ResponseEntity<>(message,HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(new ApiResponse("No exam for the given course!!!",false), HttpStatus.NO_CONTENT);
    }
    @PostMapping("/submit-exams")
    @PreAuthorize("hasAuthority('view_exam')")
    ResponseEntity<?> submitExamController(@RequestBody SubmitAnswerDto submitAnswerDto,Principal principal) throws Exception {
        Map<String,Object> message =this.examService.submitExam(submitAnswerDto,principal);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/read-exam/{examId}")
    @PreAuthorize("hasAuthority('view_exam')")
    ResponseEntity<?> getExamByIdController(@PathVariable("examId")Long examId) throws Exception {
        Map<String,Object> map=new HashMap<>();
        ExamDto exams =this.examService.getExamById(examId);
        if(exams!=null) {
            map.put("status", 200);
            map.put("data",exams);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
        return new ResponseEntity<>(null,HttpStatusCode.valueOf(200));
    }

}
