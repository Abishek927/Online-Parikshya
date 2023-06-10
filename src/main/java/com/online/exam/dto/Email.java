package com.online.exam.dto;

import com.online.exam.constant.AppConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Email {
    String to;
    String from;
    String subject;
    String text;
    Map<String, Object> properties;

    String template = AppConstant.EmailTemplatePage;
}
