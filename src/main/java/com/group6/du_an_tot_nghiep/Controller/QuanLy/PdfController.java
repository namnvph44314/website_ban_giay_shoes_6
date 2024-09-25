package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Service.QuanLy.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
@RequestMapping("/api")
public class PdfController {


    @Autowired
    private PdfService pdfService;

    @GetMapping("/pdf/{idHoaDon}")
    public void generatePDF(HttpServletResponse response, @PathVariable("idHoaDon") Integer idHoaDon) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        this.pdfService.orderCouter(response, idHoaDon);
    }
}
