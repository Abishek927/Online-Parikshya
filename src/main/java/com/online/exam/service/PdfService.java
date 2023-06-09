package com.online.exam.service;

import java.io.ByteArrayInputStream;

public interface PdfService {
    public ByteArrayInputStream createPdf(Long resultId);
}
