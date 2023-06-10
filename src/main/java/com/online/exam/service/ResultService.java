package com.online.exam.service;

import com.online.exam.dto.ResultDto;
import com.online.exam.model.Result;
import org.springframework.data.jpa.repository.Query;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

public interface ResultService {
    String createResult(Long studentExamAnswerId) throws ParseException;
    List<ResultDto> viewResult(Principal principal);


}
