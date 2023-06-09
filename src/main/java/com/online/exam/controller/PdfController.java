package com.online.exam.controller;

import com.online.exam.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController()
@RequestMapping("/pdf")
public class PdfController {
    @Autowired
    private PdfService pdfService;

    @PostMapping("/createPdf/{resultId}")
    public ResponseEntity<InputStreamResource> createPdf(@PathVariable Long resultId){

        ByteArrayInputStream pdf =pdfService.createPdf(resultId);
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-Disposition","inline;file=lcwd.pdf");
        return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));

    }



}
