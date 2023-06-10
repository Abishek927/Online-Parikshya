package com.online.exam.controller;

import com.online.exam.dto.ResultDto;
import com.online.exam.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/view")
    @PreAuthorize("hasAuthority('view_result')")
    ResponseEntity<Map<String,Object>> viewResult(Principal principal){
        Map<String,Object> map=new HashMap<>();
        List<ResultDto> result=resultService.viewResult(principal);
        map.put("status",200);
        map.put("data",result);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }


}
