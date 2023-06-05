package com.online.exam.controller;

import com.online.exam.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/result")
public class ResultController {
    @Autowired
    private ResultService resultService;
    @PostMapping("/create/{seaId}")
    ResponseEntity<String> createResultController(@PathVariable Long seaId) throws ParseException {
        String message=resultService.createResult(seaId);
        return ResponseEntity.ok().body(message);
    }


}
