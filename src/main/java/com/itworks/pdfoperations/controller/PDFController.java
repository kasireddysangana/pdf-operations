package com.itworks.pdfoperations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itworks.pdfoperations.model.MainInvoice;
import com.itworks.pdfoperations.pojo.Response;
import com.itworks.pdfoperations.service.PDFService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;

@RestController
public class PDFController {
    private Logger logger = LoggerFactory.getLogger(PDFController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private PDFService pdfService;

    @Produces(MediaType.APPLICATION_JSON)
    @RequestMapping(value = "/api/invoice/pdf", method = RequestMethod.POST)
    public Response generatePDF(@Valid @RequestBody String request) {
        String responseMessage="Unable to Create PDF";
        String pdfPath = null;
        int responseCode = 400;

        Long Starttime = System.currentTimeMillis();
        logger.info("Requested post data : " + request);

        ObjectMapper mapper = new ObjectMapper();
        MainInvoice invoice = new MainInvoice();
        /* Converting request to MainInvoice Object */
        try {
            invoice = mapper.readValue(request, MainInvoice.class);
        }catch (IOException e) {
            logger.error("PDF Controller  : generatePDF :" + "Invalid Request :" + request);
            e.printStackTrace();
        }

        /* Calling pdf service to generate PDF File */
        try {
            String message = pdfService.generatePDF(invoice);
            String []messageParts = message.split(";");

            responseCode = 200;
            responseMessage =messageParts[0];
            pdfPath = messageParts[1];
        }catch (IOException e) {
            logger.error("IOException");
            e.printStackTrace();
        }catch (DocumentException e) {
            logger.error("DocumentException");
            e.printStackTrace();
        }

        Long Endtime = System.currentTimeMillis();
        float sec = (Endtime - Starttime) / 1000F;
        logger.info("Response time taken " + sec + " : seconds");
        return new Response(responseCode, responseMessage, pdfPath);
    }

}
