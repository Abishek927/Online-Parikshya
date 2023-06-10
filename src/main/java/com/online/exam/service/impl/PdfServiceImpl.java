package com.online.exam.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.online.exam.model.Result;
import com.online.exam.model.User;
import com.online.exam.repo.CourseRepo;
import com.online.exam.repo.ExamRepo;
import com.online.exam.repo.ResultRepo;
import com.online.exam.repo.UserRepo;
import com.online.exam.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class PdfServiceImpl implements PdfService {
    @Autowired
    private ResultRepo resultRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private ExamRepo examRepo;


    @Override
    public ByteArrayInputStream createPdf(Long resultId) {

        String title="Welcome to OES result!!";
        String content="We provide Online examination services";

        Result result=resultRepo.getReferenceById(resultId);
        String userName=result.getUser().getUserName();
        String examName=result.getExam().getExamTitle();
        String marksObtained=String.valueOf(result.getMarksObtained());
        String correctChoice=String.valueOf(result.getCorrectChoice());
        String percentage=String.valueOf(result.getPercentage());
        String resultStatus=result.getResultStatus();

        Map<String,String> map=new HashMap<>();
        map.put("userName",userName);
        map.put("marksObtained",marksObtained);
        map.put("examName",examName);
        map.put("correctChoice",correctChoice);
        map.put("percentage",percentage);
        map.put("resulStatus",resultStatus);

        ByteArrayOutputStream  byteArrayOutputStream=new ByteArrayOutputStream();
        Document document=new Document(PageSize.A4);
        PdfWriter.getInstance(document,byteArrayOutputStream);

        HeaderFooter footer=new HeaderFooter(true,new Phrase("LCWD"));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setBorderWidthBottom(0);
        document.setFooter(footer);

        document.open();

        Font titleFont= FontFactory.getFont(FontFactory.HELVETICA,35);
        Paragraph titleParagraph=new Paragraph(title,titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(titleParagraph);

        Font paraFont=FontFactory.getFont(FontFactory.HELVETICA,18);
        Paragraph paragraph=new Paragraph(content,paraFont);
        document.add(paragraph);

        PdfPTable table=new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);

        writeResultHeader(table);
        writeResultData(table,map);
        document.add(table);
        document.close();

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

    }

    private void writeResultData(PdfPTable pdfPTable, Map<String,String> map){
        for(Map.Entry<String,String> eachResult:map.entrySet()){
            pdfPTable.addCell(eachResult.getKey());
            pdfPTable.addCell(eachResult.getValue());

        }
    }

    private void writeResultHeader(PdfPTable pdfPTable){
        PdfPCell pdfPCell=new PdfPCell();
        pdfPCell.setBackgroundColor(Color.LIGHT_GRAY);
        pdfPCell.setPadding(5);
        Font font=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.WHITE);
        pdfPCell.setPhrase(new Phrase("Result Categories",font));
        pdfPTable.addCell(pdfPCell);
        pdfPCell.setPhrase(new Phrase("Result Data",font));
        pdfPTable.addCell(pdfPCell);
    }


}
