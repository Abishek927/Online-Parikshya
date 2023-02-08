/*
package com.online.exam.controller;

import com.online.exam.helper.ApiResponse;
import com.online.exam.model.Question;
import com.online.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @PostMapping("/user/{uId}/fac/{facId}/cat/{catId}/course/{cId}/create")
    ResponseEntity<?> createQuestionController(@PathVariable Long uId, @PathVariable Long facId, @PathVariable Long catId, @PathVariable Long cId, @RequestBody Question question) throws Exception {
        Question resultQuestion=this.questionService.createQuestion(uId, facId, catId, cId, question);
        if(resultQuestion!=null){
            return new ResponseEntity<>(resultQuestion, HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(new ApiResponse("Something went wrong!!!",false),HttpStatusCode.valueOf(500));

    }

    @PutMapping("/user/{uId}/fac/{facId}/cat/{catId}/course/{cId}/delete")
    ResponseEntity<?> deleteQuestionController(@PathVariable Long uId,@PathVariable Long facId,@PathVariable Long catId,@PathVariable Long cId,@RequestParam("qusId")Long qusId) throws Exception {
        String deleteMessage=this.questionService.deleteQuestion(uId, facId, catId, cId, qusId);
        if(deleteMessage.contains("deleted")){
            return new ResponseEntity<>(new ApiResponse(deleteMessage,true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(new ApiResponse(deleteMessage,false),HttpStatusCode.valueOf(200));
    }

    @PutMapping("/user/{uId}/fac/{facId}/cat/{catId}/course/{cId}/update")
    ResponseEntity<?> updateQuestionController(@PathVariable Long uId,@PathVariable Long facId,@PathVariable Long catId,@PathVariable Long cId,@RequestParam("qusId")Long qusId,@RequestBody Question question) throws Exception {
        Question resultQuestion=this.questionService.updateQuestion(uId, facId, catId, cId, qusId, question);
        if(resultQuestion!=null){
            return new ResponseEntity<>(new ApiResponse("something went wrong!!!",false),HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(resultQuestion,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/user/{uId}/fac/{facId}/cat/{catId}/course/{cId}/read")
    ResponseEntity<?> getQuestionByCourse(@PathVariable Long uId,@PathVariable Long facId,@PathVariable Long catId,@PathVariable Long cId) throws Exception {
        List<Question> questions =this.questionService.getQuestionByCourse(uId, facId, catId, cId);
        if(questions.isEmpty()){
            return new ResponseEntity<>(new ApiResponse("there is no question for the given course",true),HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(questions,HttpStatusCode.valueOf(200));
    }

}
*/
