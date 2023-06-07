package com.online.exam.controller;

import com.online.exam.dto.QuestionDto;
import com.online.exam.helper.ApiResponse;
import com.online.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('manage_question')")
    ResponseEntity<ApiResponse> createQuestionController(@RequestBody QuestionDto question, Principal principal) throws Exception {
        String message=this.questionService.createQuestion(question,principal);
        return new ResponseEntity<>(new ApiResponse(message,true),HttpStatusCode.valueOf(200));

    }

    @DeleteMapping ("/delete/{qusId}")
    @PreAuthorize("hasAuthority('manage_question')")
    ResponseEntity<?> deleteQuestionController(@PathVariable("qusId")Long qusId,Principal principal) throws Exception {
        String deleteMessage=this.questionService.deleteQuestion(qusId,principal);
            return new ResponseEntity<>(new ApiResponse(deleteMessage,true),HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('manage_question')")
    ResponseEntity<Map<Integer,String>> updateQuestionController(@RequestBody QuestionDto question, Principal principal) throws Exception {
        Map<Integer,String> message=this.questionService.updateQuestion(question,principal);
        if(message.containsKey(500)){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/readByCourse/{cId}")
    @PreAuthorize("hasAuthority('view_question')")
    ResponseEntity<?> getQuestionByCourse(@PathVariable Long cId,Principal principal){
        Map<String,List<QuestionDto>> message=new HashMap<>();
        List<QuestionDto> questions =this.questionService.getQuestionByCourse(cId,principal);
        if(questions.isEmpty()){
            return new ResponseEntity<>(new ApiResponse("there is no question for the given course",true),HttpStatusCode.valueOf(200));
        }
        message.put("data",questions);
        return new ResponseEntity<>(message,HttpStatusCode.valueOf(200));
    }
    @GetMapping("/read/{qId}")
    @PreAuthorize("hasAuthority('manage_question')")
    ResponseEntity<?> getQuestionById(@PathVariable Long qId,Principal principal){
        QuestionDto question =this.questionService.getQuestionById(qId,principal);
        if(question==null){
            return new ResponseEntity<>(new ApiResponse("there is no question",true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(question,HttpStatusCode.valueOf(200));
    }


}
