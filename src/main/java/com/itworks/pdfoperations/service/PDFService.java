package com.itworks.pdfoperations.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itworks.pdfoperations.model.MainInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;

import javax.mail.*;

import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PDFService {
    private static Logger logger = LoggerFactory.getLogger(PDFService.class);

    @Autowired
    Environment environment;

    @Autowired
    MailService mailService;

    public String generatePDF(MainInvoice invoice) throws IOException, DocumentException, MessagingException {
        logger.info("PDFService :: generatePDF : Started...");

        String FoldPath = environment.getProperty("pdfFolderPath");
        String imagePath = null;
        String emailId = null;

        if(invoice.getInvoiceDetails().getLocationDetails().getLocationImagePath() != null){
            //imagePath = environment.getProperty("clinicImagePath");
            imagePath = invoice.getInvoiceDetails().getLocationDetails().getLocationImagePath();
        }
        if(invoice.getInvoiceDetails().getCustomerDetails().getCustomerEmail() != null){
            emailId = invoice.getInvoiceDetails().getCustomerDetails().getCustomerEmail();
        }
        String message = "Unable to Create PDF or Send Mail";
        String filName = "" + invoice.getInvoiceDetails().getCustomerDetails().getCustomerId() + "_" + invoice.getInvoiceId();
        String filePath = FoldPath + "/" + "" + filName + ".pdf";

        logger.info("PDFService :: generatePDF : FilePath :"+filePath);
        OutputStream file = new FileOutputStream(new File(filePath));
        Document document = new Document();
        PdfWriter.getInstance(document, file);

        Chunk connect = new Chunk(new LineSeparator(0.5f, 97, BaseColor.BLACK, Element.ALIGN_CENTER, 1.5f));
        Paragraph p = new Paragraph();
        p.add(connect);

        double a = 0;
        double b = 0;

        Font font1 = new Font(Font.FontFamily.HELVETICA);
        font1.setStyle(5);
        font1.setSize(15.0F);
        font1.setColor(BaseColor.RED);

        Font font2 = new Font(Font.FontFamily.HELVETICA);
        font2.setStyle(5);
        font2.setSize(10.0F);

        Font font3 = new Font(Font.FontFamily.HELVETICA, Font.NORMAL);
        font3.setStyle(2);
        font3.setSize(8.0F);

        Font font4 = new Font(Font.FontFamily.HELVETICA);
        font4.setStyle(1);
        font4.setSize(10.0F);

        Font font5 = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
        font5.setStyle("bold");

        Font font6 = new Font(Font.FontFamily.HELVETICA, Font.NORMAL);
        font6.setStyle(2);
        font6.setSize(8.0F);
        font6.setColor(BaseColor.RED);
        Image image=null;

        if(imagePath!= null) {
             image = Image.getInstance(imagePath);
            image.scaleAbsolute(80.0F, 40.0F);
            image.setAlignment(0);
        }

        document.open();

        PdfPTable header = new PdfPTable(2);
        header.setWidthPercentage(98);
        header.setComplete(true);
        PdfPCell headercell = new PdfPCell(new Paragraph(new Chunk("Invoice", font2)));
        headercell.setColspan(2);
        headercell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headercell.setPadding(4.0f);
        headercell.setBackgroundColor(new BaseColor(51, 175, 255));
        headercell.setBorder(Rectangle.NO_BORDER);
        header.addCell(headercell);
        PdfPCell headercell1 = new PdfPCell(new Paragraph(new Chunk("", font4)));
        headercell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        headercell1.setBorder(Rectangle.NO_BORDER);
        header.addCell(headercell1);
        PdfPCell headercell2 = new PdfPCell(new Paragraph(new Chunk("", font3)));
        headercell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headercell2.setPadding(0.0f);
        headercell2.setBorder(Rectangle.NO_BORDER);
        header.addCell(headercell2);
        PdfPCell headercell3 = new PdfPCell(new Paragraph(new Chunk("", font3)));
        headercell3.setHorizontalAlignment(Element.ALIGN_LEFT);
        headercell3.setPadding(0.0f);
        headercell3.setBorder(Rectangle.NO_BORDER);
        header.addCell(headercell3);
        PdfPCell headercell4=null;
        if(image != null) {
             headercell4 = new PdfPCell(image, false);
            headercell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headercell4.setPadding(0.0f);
            headercell4.setBorder(Rectangle.NO_BORDER);
            header.addCell(headercell4);
        }else{
             headercell4 = new PdfPCell();
            headercell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headercell4.setPadding(0.0f);
            headercell4.setBorder(Rectangle.NO_BORDER);
            header.addCell(headercell4);
        }

        document.add(header);
        document.add(p);


        PdfPTable header2 = new PdfPTable(1);
        header2.setWidthPercentage(97);

        PdfPCell headercella1 = new PdfPCell(new Paragraph(new Chunk("Clinic Details", font4)));
        headercella1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headercella1.setBorder(Rectangle.NO_BORDER);
        header2.addCell(headercella1);

        PdfPCell headercella2 = new PdfPCell(
                new Paragraph(new Chunk("Name:" + invoice.getInvoiceDetails().getLocationDetails().getLocationName(), font3)));
        headercella2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headercella2.setPadding(0.0f);
        headercella2.setBorder(Rectangle.NO_BORDER);
        header2.addCell(headercella2);

        PdfPCell headercella3 = new PdfPCell(new Paragraph(new Chunk("Contact No.:" + invoice.getInvoiceDetails().getLocationDetails().getLocationNumber(),
                font3)));
        headercella3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headercella3.setPadding(0.0f);
        headercella3.setBorder(Rectangle.NO_BORDER);
        header2.addCell(headercella3);

        PdfPCell headercella4 = new PdfPCell(new Paragraph(new Chunk("Address :" + invoice.getInvoiceDetails().getLocationDetails().getLocationAddress(),
                font3)));
        headercella4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headercella4.setPadding(0.0f);
        headercella4.setBorder(Rectangle.NO_BORDER);
        header2.addCell(headercella4);
        document.add(header2);
        document.add(p);


        PdfPTable patientDetails = new PdfPTable(2);
        patientDetails.setWidthPercentage(97);

        PdfPCell patientDetailsH = new PdfPCell(new Paragraph(new Chunk("Patient details", font4)));
        patientDetailsH.setBorder(Rectangle.NO_BORDER);
        patientDetails.addCell(patientDetailsH);


        PdfPCell patientDetailsHHH = new PdfPCell(new Paragraph(new Chunk("", font3)));
        patientDetailsHHH.setBorder(Rectangle.NO_BORDER);
        patientDetails.addCell(patientDetailsHHH);

        PdfPCell patientName = new PdfPCell(new Paragraph(new Chunk("Name :" + invoice.getInvoiceDetails().getCustomerDetails().getCustomerName(), font3)));
        patientName.setHorizontalAlignment(Element.ALIGN_LEFT);
        patientName.setPadding(0.0f);
        patientName.setBorder(Rectangle.NO_BORDER);
        patientDetails.addCell(patientName);


        PdfPCell contactNoooo = new PdfPCell(new Phrase("Patient Id :" + invoice.getInvoiceDetails().getCustomerDetails().getCustomerId(), font3));
        contactNoooo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        contactNoooo.setPadding(0.0f);
        contactNoooo.setBorder(Rectangle.NO_BORDER);
        patientDetails.addCell(contactNoooo);


        PdfPCell contactNo = new PdfPCell(new Phrase("contactNo. : " + invoice.getInvoiceDetails().getCustomerDetails().getCustomerPhoneNumber(), font3));
        contactNo.setHorizontalAlignment(Element.ALIGN_LEFT);
        contactNo.setPadding(0.0f);
        contactNo.setBorder(Rectangle.NO_BORDER);
        patientDetails.addCell(contactNo);


        PdfPCell contactNooo = new PdfPCell(new Phrase("Invoice Id : " + invoice.getInvoiceId(), font3));
        contactNooo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        contactNooo.setPadding(0.0f);
        contactNooo.setBorder(Rectangle.NO_BORDER);
        patientDetails.addCell(contactNooo);

        PdfPCell age = new PdfPCell(new Paragraph(new Chunk("Age : " + invoice.getInvoiceDetails().getCustomerDetails().getCustomerAge(), font3)));
        age.setHorizontalAlignment(Element.ALIGN_LEFT);
        age.setPadding(0.0f);
        age.setBorder(Rectangle.NO_BORDER);
        patientDetails.addCell(age);


        PdfPCell ageee = new PdfPCell(new Paragraph(new Chunk("", font3)));
        ageee.setHorizontalAlignment(Element.ALIGN_RIGHT);
        ageee.setPadding(0.0f);
        ageee.setBorder(Rectangle.NO_BORDER);
        patientDetails.addCell(ageee);

        float[] columnWidths11 = {60.0F, 60.0F};
        patientDetails.setWidths(columnWidths11);

        document.add(patientDetails);
        document.add(p);

        int noOfServiceRequests = invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().size();


        for (int i1 = 0; i1 < noOfServiceRequests; i1++) {

            PdfPTable PackageBilll = new PdfPTable(1);
            PackageBilll.setWidthPercentage(97);

            PdfPCell PackageBill11 = new PdfPCell(new Paragraph(new Chunk("" + invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().get(i1).getServiceRequestDate(), font3)));
            PackageBill11.setHorizontalAlignment(Element.ALIGN_LEFT);
            PackageBill11.setBorder(Rectangle.NO_BORDER);
            PackageBilll.addCell(PackageBill11);
            document.add(PackageBilll);


            PdfPTable PackageBill = new PdfPTable(4);
            PackageBill.setWidthPercentage(97);

            PdfPCell PackageBill1 = new PdfPCell(new Paragraph(new Chunk("" +invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().get(i1).getServiceName(), font6)));
            PackageBill1.setHorizontalAlignment(Element.ALIGN_LEFT);
            PackageBill1.setBorder(Rectangle.NO_BORDER);
            PackageBill.addCell(PackageBill1);

            PdfPCell PackageBill2 = new PdfPCell(new Paragraph(new Chunk("", font3)));
            PackageBill2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PackageBill2.setPadding(0.0f);
            PackageBill2.setBorder(Rectangle.NO_BORDER);
            PackageBill.addCell(PackageBill2);

            PdfPCell PackageBill8 = new PdfPCell(new Phrase("Total : INR." + invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().get(i1).getTotalAmount(), font3));
            PackageBill8.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PackageBill8.setPadding(0.0f);
            PackageBill8.setBorder(Rectangle.NO_BORDER);
            PackageBill.addCell(PackageBill8);

            PdfPCell PackageBill7 = new PdfPCell(new Paragraph(new Chunk("Due : INR." + invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().get(i1).getBalanceAmount(), font3)));
            PackageBill7.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PackageBill7.setPadding(0.0f);
            PackageBill7.setBorder(Rectangle.NO_BORDER);
            PackageBill.addCell(PackageBill7);

            float[] columnWidths1 = {30.0F, 35.0F, 25.0F, 24.0F};
            PackageBill.setWidths(columnWidths1);
            document.add(PackageBill);

            PdfPTable PackageBillll = new PdfPTable(1);
            PackageBillll.setWidthPercentage(97);
            PdfPCell PackageBill111 = new PdfPCell(new Paragraph(new Chunk(invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().get(i1).getDoctorName(), font3)));
            PackageBill111.setHorizontalAlignment(Element.ALIGN_LEFT);
            PackageBill111.setBorder(Rectangle.NO_BORDER);
            PackageBillll.addCell(PackageBill111);
            document.add(PackageBillll);

        }
        document.add(p);

        PdfPTable AmountDate = new PdfPTable(1);
        AmountDate.setWidthPercentage(97);

        PdfPCell AmountDate2 = new PdfPCell(new Paragraph(new Chunk("Total : INR." + invoice.getInvoiceDetails().getTotalInvoiceAmount(), font3)));
        AmountDate2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        AmountDate2.setPadding(0.0f);
        AmountDate2.setBorder(Rectangle.NO_BORDER);
        AmountDate.addCell(AmountDate2);

        PdfPCell AmountDate3 = new PdfPCell(new Phrase("Discount : INR."  + invoice.getInvoiceDetails().getTotalDiscountAmount(), font3));
        AmountDate3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        AmountDate3.setPadding(0.0f);
        AmountDate3.setBorder(Rectangle.NO_BORDER);
        AmountDate.addCell(AmountDate3);

        PdfPCell AmountDate4 = new PdfPCell(new Paragraph(new Chunk("Paid : INR. " + invoice.getInvoiceDetails().getTotalAmountPaid(), font3)));
        AmountDate4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        AmountDate4.setPadding(0.0f);
        AmountDate4.setBorder(Rectangle.NO_BORDER);
        AmountDate.addCell(AmountDate4);

        PdfPCell AmountDate5 = new PdfPCell(new Paragraph(new Chunk("Due : INR." + invoice.getInvoiceDetails().getInvoiceBalanceAmount(), font3)));
        AmountDate5.setHorizontalAlignment(Element.ALIGN_RIGHT);
        AmountDate5.setPadding(0.0f);
        AmountDate5.setBorder(Rectangle.NO_BORDER);
        AmountDate.addCell(AmountDate5);

        document.add(AmountDate);
        document.add(p);

        PdfPTable header22 = new PdfPTable(1);
        header22.setWidthPercentage(97);

        PdfPCell headercel1la1 = new PdfPCell(new Paragraph(new Chunk("Payment Details", font4)));
        headercel1la1.setHorizontalAlignment(Element.ALIGN_LEFT);
        headercel1la1.setBorder(Rectangle.NO_BORDER);
        header22.addCell(headercel1la1);

        int noOfPaymentsDone = invoice.getInvoiceDetails().getPaymentDetails().size();

        for (int i1 = 0; i1 < noOfPaymentsDone; i1++) {

            PdfPCell headercellla2 = new PdfPCell(
                    new Paragraph(new Chunk("" + invoice.getInvoiceDetails().getPaymentDetails().get(i1).getPaymentDate() +
                            " Paid : INR. "+ invoice.getInvoiceDetails().getPaymentDetails().get(i1).getPaymentAmount() +
                            "("+invoice.getInvoiceDetails().getPaymentDetails().get(i1).getPaymentMethod() +")", font3)));
            headercellla2.setHorizontalAlignment(Element.ALIGN_LEFT);
            headercellla2.setPadding(0.0f);
            headercellla2.setBorder(Rectangle.NO_BORDER);
            header22.addCell(headercellla2);

        }
        document.add(header22);


        document.close();
        file.close();
        message = "PDF Created successfully and could not send mail as no mail id is given;" + filePath;
        logger.info("PDFService :: generatePDF : Emailds ::"+invoice.getInvoiceDetails().getCustomerDetails().getCustomerEmail());

        try {
            System.out.println("Email ID : " + emailId);
            /* If email id is not null call mail service to send mail */
            if(emailId!= null) {
                message = mailService.sendMail(filePath, document, emailId);
            }
        }catch(MessagingException me){
            message = "PDF Creted Successfully, but unable to send mail;" + filePath;
            logger.error("PDFService :: generatePDF : Unable to send Email :"+me);
            me.printStackTrace();
        }finally {
            file.close();
        }

        return message;
    }



}
