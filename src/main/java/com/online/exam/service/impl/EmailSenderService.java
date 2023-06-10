package com.online.exam.service.impl;

import com.online.exam.constant.AppConstant;
import com.online.exam.dto.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;
import java.nio.charset.StandardCharsets;

@Service
public class EmailSenderService {

    private JavaMailSender javaMailSender;
    private SpringTemplateEngine springTemplateEngine;

    public EmailSenderService(JavaMailSender javaMailSender,SpringTemplateEngine springTemplateEngine){
        this.javaMailSender=javaMailSender;
        this.springTemplateEngine=springTemplateEngine;
    }

    public void sendHtmlMessage(Email email) throws MessagingException {
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper=new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
        Context context=new Context();
        context.setVariables(email.getProperties());
        messageHelper.setFrom(email.getFrom());
        messageHelper.setTo(email.getTo());
        messageHelper.setSubject(email.getSubject());
        String html=springTemplateEngine.process(email.getTemplate(),context);
        messageHelper.setText(html,true);
        javaMailSender.send(mimeMessage);

    }


    public String sendSimpleMail(Email details) {

        // Creating a simple mail message
        SimpleMailMessage mailMessage
                = new SimpleMailMessage();

        // Setting up necessary details
        mailMessage.setFrom(details.getFrom());
        mailMessage.setTo(details.getTo());
        mailMessage.setText(details.getText());
        mailMessage.setSubject(details.getSubject());

        // Sending the mail
        javaMailSender.send(mailMessage);
        return "Mail Sent Successfully...";
    }
}
