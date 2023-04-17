package com.online.exam.controller;

import com.online.exam.helper.ApiResponse;
import com.online.exam.model.Question;
import com.online.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @PostMapping("/user/{uId}/course/{cId}/create")
    @PreAuthorize("hasAuthority('manage_question')")
    ResponseEntity<?> createQuestionController(@PathVariable Long uId, @PathVariable Long cId, @RequestBody Question question) throws Exception {
        Question resultQuestion=this.questionService.createQuestion(uId,cId, question);
        if(resultQuestion!=null){
            return new ResponseEntity<>(resultQuestion, HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(new ApiResponse("Something went wrong!!!",false),HttpStatusCode.valueOf(500));

    }

    @DeleteMapping ("/user/{uId}/delete/{qusId}")
    @PreAuthorize("hasAuthority('manage_question')")
    ResponseEntity<?> deleteQuestionController(@PathVariable Long uId,@PathVariable("qusId")Long qusId) throws Exception {
        String deleteMessage=this.questionService.deleteQuestion(uId,qusId);
        if(deleteMessage.contains("deleted")){
            return new ResponseEntity<>(new ApiResponse(deleteMessage,true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(new ApiResponse(deleteMessage,false),HttpStatusCode.valueOf(200));
    }

    @PutMapping("/user/{uId}/update/{qusId}")
    @PreAuthorize("hasAuthority('manage_question')")
    ResponseEntity<?> updateQuestionController(@PathVariable Long uId,@PathVariable("qusId")Long qusId,@RequestBody Question question) throws Exception {
        Question resultQuestion=this.questionService.updateQuestion(uId,qusId, question);
        if(resultQuestion==null){
            return new ResponseEntity<>(new ApiResponse("something went wrong!!!",false),HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(resultQuestion,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/user/{uId}/course/{cId}/read")
    @PreAuthorize("hasAuthority('manage_question')")
    ResponseEntity<?> getQuestionByCourse(@PathVariable Long uId,@PathVariable Long cId){
        List<Question> questions =this.questionService.getQuestionByCourse(uId, cId);
        if(questions.isEmpty()){
            return new ResponseEntity<>(new ApiResponse("there is no question for the given course",true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(questions,HttpStatusCode.valueOf(200));
    }


}
