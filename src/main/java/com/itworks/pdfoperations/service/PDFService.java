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
import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PDFService {
    private static Logger logger = LoggerFactory.getLogger(PDFService.class);

    @Autowired
    private Environment environment;

    @Autowired
    private MailService mailService;

    public String generatePDF(MainInvoice invoice) throws IOException, DocumentException {
        logger.info("PDFService :: generatePDF : Started...");

        String FoldPath = environment.getProperty("pdfFolderPath");
        String imagePath = null;
        String headerPath = null;
        String emailId = null;

        DecimalFormat df2 = new DecimalFormat("####0.00");

        if(invoice.getInvoiceDetails().getLocationDetails().getLocationHeaderPath() != null){
            //imagePath = environment.getProperty("clinicImagePath");
            headerPath = invoice.getInvoiceDetails().getLocationDetails().getLocationHeaderPath();
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

        Chunk chunk = new Chunk(new LineSeparator(0.0f, 00, BaseColor.LIGHT_GRAY, Element.ALIGN_JUSTIFIED, 0.0f));
        chunk.setUnderline(0.1f, 0.1f);

        Phrase phrase = new Phrase(0);
        phrase.add(chunk);
        Paragraph p = new Paragraph();
        p.setSpacingAfter(2);
        p.add(chunk);
        p.setSpacingBefore(2);

        Font font1 = new Font(Font.FontFamily.HELVETICA);
        font1.setStyle(1);
        font1.setSize(12.0F);

        Font font2 = new Font(Font.FontFamily.HELVETICA);
        font2.setStyle(5);
        font2.setSize(10.0F);

        // normalTextFont = font3
        Font normalTextFont = new Font(Font.FontFamily.HELVETICA, Font.NORMAL);
        normalTextFont.setStyle(2);
        normalTextFont.setSize(8.0F);

        Font serviceNameTextFont = new Font(Font.FontFamily.HELVETICA, Font.NORMAL);
        serviceNameTextFont.setStyle(2);
        serviceNameTextFont.setSize(8.0F);


        Font font4 = new Font(Font.FontFamily.HELVETICA);
        font4.setStyle(1);
        font4.setSize(10.0F);

        Font red = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.RED);
        Font font5 = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
        font5.setStyle("bold");

        // invoiceDetailsFont = font6
        Font invoiceDetailsFont = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
        invoiceDetailsFont.setStyle("bold");

        Font paymentDetailsHeaderFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
        paymentDetailsHeaderFont.setStyle("bold");

        Image header = null;

        if (headerPath != null) {
            header = Image.getInstance(headerPath);
            header.scaleAbsolute(600.0F, 120.0F);
            header.setAlignment(1);
        }

        document.open();

        document.add(header);


        /*document.add(new Paragraph(new Chunk("Invoices", font1)));*/
        PdfPTable invoices = new PdfPTable(1);
        invoices.setWidthPercentage(100);

        PdfPCell invoicesCell1 = new PdfPCell(new Paragraph(new Chunk("Invoice", font1)));
        invoicesCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoicesCell1.setBorder(Rectangle.NO_BORDER);

        invoices.setSpacingBefore(8.0F);
        invoices.setSpacingAfter(8.0F);

        invoices.addCell(invoicesCell1);
        document.add(invoices);

        /* Generate horizotalbar */
        document.add(new LineSeparator(0.5f, 100, null, 0, -5));

        String patientName = invoice.getInvoiceDetails().getCustomerDetails().getCustomerName().toUpperCase();
        int patientId = invoice.getInvoiceDetails().getCustomerDetails().getCustomerId();

        String patientGender = invoice.getInvoiceDetails().getCustomerDetails().getCustomerGender();
        String patientAge = invoice.getInvoiceDetails().getCustomerDetails().getCustomerAge();
        String patientAddress = invoice.getInvoiceDetails().getCustomerDetails().getCustomerAddress();
        String patientPhoneNumber = invoice.getInvoiceDetails().getCustomerDetails().getCustomerPhoneNumber();


        Paragraph paragraph = new Paragraph(new Chunk(patientName + "(" + patientId + ")", normalTextFont));
        paragraph.setSpacingBefore(5.0F);
        document.add(paragraph);

        document.add(new Paragraph(new Chunk(patientGender + ", " + patientAge, normalTextFont)));
        document.add(new Paragraph(new Chunk(patientAddress + "\r\n"+ patientPhoneNumber, normalTextFont)));

        document.add(new LineSeparator(0.5f, 100, null, 0, -5));

        /* Populating Invoice Details */
        PdfPTable invoiceDetails = new PdfPTable(4);
        invoiceDetails.setWidthPercentage(100);

        PdfPCell invoiceDetailsCell2 = new PdfPCell(new Paragraph(new Chunk(
                "", invoiceDetailsFont)));
        invoiceDetailsCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceDetailsCell2.setBorder(Rectangle.NO_BORDER);

        invoiceDetails.addCell(invoiceDetailsCell2);

        PdfPCell invoiceDetailsCell3 = new PdfPCell(new Paragraph(new Chunk(
                "", invoiceDetailsFont)));
        invoiceDetailsCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceDetailsCell3.setBorder(Rectangle.NO_BORDER);

        invoiceDetails.addCell(invoiceDetailsCell3);

        PdfPCell invoiceDetailsCell4 = new PdfPCell(new Paragraph(new Chunk("Date:",normalTextFont)));
        invoiceDetailsCell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceDetailsCell4.setPadding(5.0f);
        invoiceDetailsCell4.setBorder(Rectangle.NO_BORDER);

        invoiceDetails.addCell(invoiceDetailsCell4);

        PdfPCell invoiceDetailsCell5 = new PdfPCell(new Paragraph(new Chunk(""+invoice.getInvoiceDetails().getInvoiceDate(),invoiceDetailsFont)));
        invoiceDetailsCell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceDetailsCell5.setPadding(5.0f);
        invoiceDetailsCell5.setBorder(Rectangle.NO_BORDER);

        invoiceDetails.addCell(invoiceDetailsCell5);

        invoiceDetails.setSpacingBefore(6.1F);
        invoiceDetails.setSpacingAfter(2.1F);

        float[] columnWidths1155759 = { 40.0F,30.0F,20.0F,12.0F };

        invoiceDetails.setWidths(columnWidths1155759);

        document.add(invoiceDetails);

        PdfPTable invoiceNumber = new PdfPTable(5);
        invoiceNumber.setWidthPercentage(100);

        PdfPCell invoiceNumberCell1 = new PdfPCell(new Paragraph(new Chunk("", font1)));
        invoiceNumberCell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceNumberCell1.setBorder(Rectangle.NO_BORDER);
        invoiceNumber.addCell(invoiceNumberCell1);

        PdfPCell invoiceNumberCell2 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        invoiceNumberCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceNumberCell2.setBorder(Rectangle.NO_BORDER);
        invoiceNumber.addCell(invoiceNumberCell2);

        PdfPCell invoiceNumberCell3 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        invoiceNumberCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceNumberCell3.setBorder(Rectangle.NO_BORDER);
        invoiceNumber.addCell(invoiceNumberCell3);


        PdfPCell invoiceNumberCell4 = new PdfPCell(new Phrase("Invoice Number:", normalTextFont));
        invoiceNumberCell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceNumberCell4.setPadding(0.0f);
        invoiceNumberCell4.setBorder(Rectangle.NO_BORDER);
        invoiceNumber.addCell(invoiceNumberCell4);

        PdfPCell invoiceNumberCell5 = new PdfPCell(new Phrase(""+invoice.getInvoiceDetails().getInvoiceNumber(), invoiceDetailsFont));
        invoiceNumberCell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceNumberCell5.setPadding(1.0f);
        invoiceNumberCell5.setBorder(Rectangle.NO_BORDER);
        invoiceNumber.addCell(invoiceNumberCell5);

        invoiceNumber.setSpacingBefore(3.1F);
        invoiceNumber.setSpacingAfter(5.1F);

        float[] invoiceNumberColumnWidths = { 15.0F, 40.0F,30.0F,20.0F,13.0F };

        invoiceNumber.setWidths(invoiceNumberColumnWidths);
        document.add(invoiceNumber);

        /* Populating Treatments & Procedures Details */
        PdfPTable treatmentsAndProceduresHeader = new PdfPTable(6);
        treatmentsAndProceduresHeader.setWidthPercentage(100);

        PdfPCell treatmentsAndProceduresCell1 = new PdfPCell(new Paragraph(new Chunk(
                "# ", font4)));
        treatmentsAndProceduresCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        treatmentsAndProceduresCell1.setBorder(Rectangle.NO_BORDER);
        treatmentsAndProceduresCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        treatmentsAndProceduresHeader.addCell(treatmentsAndProceduresCell1);

        PdfPCell treatmentsAndProceduresName = new PdfPCell(new Paragraph(new Chunk(
                "Treatments & Procedures", font4)));
        treatmentsAndProceduresName.setHorizontalAlignment(Element.ALIGN_LEFT);
        treatmentsAndProceduresName.setPadding(0.0f);
        treatmentsAndProceduresName.setBorder(Rectangle.NO_BORDER);
        treatmentsAndProceduresName.setBackgroundColor(BaseColor.LIGHT_GRAY);
        treatmentsAndProceduresHeader.addCell(treatmentsAndProceduresName);


        PdfPCell treatmentsAndProceduresUnitCost = new PdfPCell(
                new Phrase("Unit Cost", font4));
        treatmentsAndProceduresUnitCost.setHorizontalAlignment(Element.ALIGN_RIGHT);
        treatmentsAndProceduresUnitCost.setPadding(0.0f);
        treatmentsAndProceduresUnitCost.setBorder(Rectangle.NO_BORDER);
        treatmentsAndProceduresUnitCost.setBackgroundColor(BaseColor.LIGHT_GRAY);
        treatmentsAndProceduresHeader.addCell(treatmentsAndProceduresUnitCost);

        PdfPCell treatmentsAndProceduresDiscount = new PdfPCell(new Paragraph(new Chunk(
                "Discount", font4)));
        treatmentsAndProceduresDiscount.setHorizontalAlignment(Element.ALIGN_RIGHT);
        treatmentsAndProceduresDiscount.setPadding(0.0f);
        treatmentsAndProceduresDiscount.setBorder(Rectangle.NO_BORDER);
        treatmentsAndProceduresDiscount.setBackgroundColor(BaseColor.LIGHT_GRAY);
        treatmentsAndProceduresHeader.addCell(treatmentsAndProceduresDiscount);

        PdfPCell treatmentsAndProceduresTotalCost = new PdfPCell(new Phrase("Total Cost",
                font4));
        treatmentsAndProceduresTotalCost.setHorizontalAlignment(Element.ALIGN_RIGHT);
        treatmentsAndProceduresTotalCost.setPadding(0.0f);
        treatmentsAndProceduresTotalCost.setBorder(Rectangle.NO_BORDER);
        treatmentsAndProceduresTotalCost.setBackgroundColor(BaseColor.LIGHT_GRAY);
        treatmentsAndProceduresHeader.addCell(treatmentsAndProceduresTotalCost);

        PdfPCell treatmentsAndProceduresamountPaid = new PdfPCell(new Phrase("Amount Paid",
                font4));
        treatmentsAndProceduresamountPaid.setHorizontalAlignment(Element.ALIGN_RIGHT);
        treatmentsAndProceduresamountPaid.setPadding(0.0f);
        treatmentsAndProceduresamountPaid.setBorder(Rectangle.NO_BORDER);
        treatmentsAndProceduresamountPaid.setBackgroundColor(BaseColor.LIGHT_GRAY);
        treatmentsAndProceduresHeader.addCell(treatmentsAndProceduresamountPaid);

        float[] treatmentsAndProceduresColumnWidth = {5.0F, 45.0F, 15.0F, 15.0F, 15.0F, 15.0F};
        treatmentsAndProceduresHeader.setWidths(treatmentsAndProceduresColumnWidth);
        treatmentsAndProceduresHeader.setSpacingBefore(0.1F);

        document.add(treatmentsAndProceduresHeader);
        treatmentsAndProceduresHeader.setSpacingAfter(0.1F);

        int noOfServiceRequests = invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().size();

        for (int i = 0; i < noOfServiceRequests; i++) {
            int j = i + 1;
            PdfPTable treatmentsAndProceduresDetails = new PdfPTable(6);
            treatmentsAndProceduresDetails.setWidthPercentage(100);

            PdfPCell serviceRequestNumber = new PdfPCell(new Paragraph(new Chunk("" + j, normalTextFont)));
            serviceRequestNumber.setHorizontalAlignment(Element.ALIGN_LEFT);
            serviceRequestNumber.setBorder(Rectangle.NO_BORDER);

            treatmentsAndProceduresDetails.addCell(serviceRequestNumber);

            String serviceName = invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().get(i).getServiceName();
            String serviceDate = invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().get(i).getServiceRequestDate();
            String doctorName = invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().get(i).getDoctorName();
            com.itextpdf.text.BaseColor colourCode = invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().get(i).getServiceNameColor();
            serviceNameTextFont.setColor(colourCode);

            PdfPCell serviceRequestName = new PdfPCell(new Paragraph(new Chunk(serviceName, serviceNameTextFont)));
            serviceRequestName.setHorizontalAlignment(Element.ALIGN_LEFT);
            serviceRequestName.setPadding(0.0f);
            serviceRequestName.setBorder(Rectangle.NO_BORDER);

            treatmentsAndProceduresDetails.addCell(serviceRequestName);

            Double unitCost = invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().get(i).getUnitCost();
            PdfPCell serviceRequestUnitCost = new PdfPCell(new Phrase(df2.format(unitCost), normalTextFont));
            serviceRequestUnitCost.setHorizontalAlignment(Element.ALIGN_RIGHT);
            serviceRequestUnitCost.setPadding(0.0f);
            serviceRequestUnitCost.setBorder(Rectangle.NO_BORDER);

            treatmentsAndProceduresDetails.addCell(serviceRequestUnitCost);

            Double discountAmount = invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().get(i).getDiscountAmount();
            PdfPCell serviceRequestDiscount = new PdfPCell(new Paragraph(new Chunk(df2.format(discountAmount) + "", normalTextFont)));
            serviceRequestDiscount.setHorizontalAlignment(Element.ALIGN_RIGHT);
            serviceRequestDiscount.setPadding(0.0f);
            serviceRequestDiscount.setBorder(Rectangle.NO_BORDER);
            treatmentsAndProceduresDetails.addCell(serviceRequestDiscount);

            double totalCost = unitCost - discountAmount;
            PdfPCell serviceRequestTotalCost = new PdfPCell(new Phrase(df2.format(totalCost) + "", normalTextFont));
            serviceRequestTotalCost.setHorizontalAlignment(Element.ALIGN_RIGHT);
            serviceRequestTotalCost.setPadding(0.0f);
            serviceRequestTotalCost.setBorder(Rectangle.NO_BORDER);
            treatmentsAndProceduresDetails.addCell(serviceRequestTotalCost);

            Double amountPaid = invoice.getInvoiceDetails().getCustomerDetails().getServiceRequestDetails().get(i).getAmountPaid();
            PdfPCell serviceRequestAmountPaid = new PdfPCell(new Phrase(df2.format(amountPaid) + "", normalTextFont));
            serviceRequestAmountPaid.setHorizontalAlignment(Element.ALIGN_RIGHT);
            serviceRequestAmountPaid.setPadding(0.0f);
            serviceRequestAmountPaid.setBorder(Rectangle.NO_BORDER);

            treatmentsAndProceduresDetails.addCell(serviceRequestAmountPaid);

            float[] columnWidths1 = {5.0F, 45.0F, 15.0F, 15.0F, 15.0F, 15.0F};
            treatmentsAndProceduresDetails.setWidths(columnWidths1);

            treatmentsAndProceduresDetails.setSpacingBefore(4.0F);
            treatmentsAndProceduresDetails.setSpacingAfter(4.0F);
            document.add(treatmentsAndProceduresDetails);

            PdfPTable date = new PdfPTable(2);
            date.setWidthPercentage(100);

            PdfPCell emptySpace = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
            emptySpace.setHorizontalAlignment(Element.ALIGN_RIGHT);
            emptySpace.setPadding(0.0f);
            emptySpace.setBorder(Rectangle.NO_BORDER);
            date.addCell(emptySpace);

            PdfPCell dateValue = new PdfPCell(new Phrase("  By " + doctorName + "      Date  " + serviceDate, normalTextFont));
            dateValue.setHorizontalAlignment(Element.ALIGN_LEFT);
            dateValue.setPadding(0.0f);
            dateValue.setBorder(Rectangle.NO_BORDER);
            date.addCell(dateValue);

            float[] columnWidths11 = { 6.0F, 94.0F };
            date.setWidths(columnWidths11);

            date.setSpacingBefore(0.0F);
            date.setSpacingAfter(4.1F);
            document.add(date);

            if (noOfServiceRequests  != i) {

                PdfPTable remittanceeeeer = new PdfPTable(1);
                remittanceeeeer.setWidthPercentage(100);
                remittanceeeeer.setHorizontalAlignment(Element.ALIGN_LEFT);

                PdfPCell remittanceSpace = new PdfPCell(new Paragraph(new Chunk("").setUnderline(0.1f, -2f)));
                remittanceSpace.setHorizontalAlignment(Element.ALIGN_LEFT);
                remittanceSpace.setPadding(0.0f);
                remittanceSpace.setBorder(Rectangle.BOTTOM);
                remittanceSpace.setUseBorderPadding(true);
                remittanceeeeer.addCell(remittanceSpace);

                document.add(remittanceeeeer);

                PdfPTable remittancee = new PdfPTable(4);
                remittancee.setWidthPercentage(100);
                remittancee.setHorizontalAlignment(Element.ALIGN_LEFT);
                PdfPCell remittance1 = new PdfPCell(new Paragraph(new Chunk("", font4)));
                remittance1.setHorizontalAlignment(Element.ALIGN_CENTER);
                remittance1.setPadding(0.0f);
                remittance1.setBorder(Rectangle.NO_BORDER);
                remittance1.setUseBorderPadding(true);
                remittancee.addCell(remittance1);

                PdfPCell remittanceg1 = new PdfPCell(new Paragraph(new Chunk("").setUnderline(0.1f, -2f)));
                remittanceg1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                remittanceg1.setPadding(0.0f);
                remittanceg1.setBorder(Rectangle.BOTTOM);
                remittanceg1.setUseBorderPadding(true);
                remittancee.addCell(remittanceg1);

                PdfPCell remittance11 = new PdfPCell(new Paragraph(new Chunk("", font4)));
                remittance11.setHorizontalAlignment(Element.ALIGN_CENTER);
                remittance11.setPadding(0.0f);
                remittance11.setBorder(Rectangle.NO_BORDER);
                remittance11.setUseBorderPadding(true);
                remittancee.addCell(remittance11);

                PdfPCell remittancegf1 = new PdfPCell(new Paragraph(new Chunk("").setUnderline(0.1f, -2f)));
                remittancegf1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                remittancegf1.setPadding(0.0f);
                remittancegf1.setBorder(Rectangle.BOTTOM);
                remittancegf1.setUseBorderPadding(true);
                remittancee.addCell(remittancegf1);
                float[] columnWidths119 = {70.0F, 10.0F, 1.0F, 10.0F};
                remittancee.setWidths(columnWidths119);

                document.add(remittancee);
                // document.add( Chunk.NEWLINE );
            }
        }

        /* Populate Total Cost Fields */
        Double invoiceTotalCost = invoice.getInvoiceDetails().getTotalInvoiceAmount();
        drawCostsAndDiscounts(document,normalTextFont, font4, invoiceTotalCost, "Total Cost");

        /* Populate Total Discount Fields */
        Double invoiceDiscount = invoice.getInvoiceDetails().getTotalDiscountAmount();
        drawCostsAndDiscounts(document,normalTextFont, font4, invoiceDiscount, "Total Discount");

        /* Populate Horizontal Bar */
        drawHorizontalLine(document, normalTextFont, font4);

        /* Populate Grand Total Fields */
        Double grandTotal = invoiceTotalCost - invoiceDiscount ;
        drawCostsAndDiscounts(document,normalTextFont, font4, grandTotal, "Grand Total");

        /* Populate Amount Received Fields */
        Double amountRecieved = invoice.getInvoiceDetails().getTotalAmountPaid();
        drawCostsAndDiscounts(document,normalTextFont, font4, amountRecieved, "Amount Received");

        /* Populate Horizontal Bar */
        drawHorizontalLine(document, normalTextFont, font4);

        /* Populate Balance Amount Fields */
        Double balanceAmount = invoice.getInvoiceDetails().getInvoiceBalanceAmount();
        drawCostsAndDiscounts(document,normalTextFont, font4, balanceAmount, "Balance Amount");

        /* Populate Horizontal Bar */
        drawHorizontalLine(document, normalTextFont, font4);

        /* Populate Payment Details */
        PdfPTable paymentDetailsHeader = new PdfPTable(1);
        paymentDetailsHeader.setWidthPercentage(70);
        paymentDetailsHeader.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell paymentDetailsHeaderName = new PdfPCell(new Paragraph(new Chunk("Payment Details", paymentDetailsHeaderFont)));
        paymentDetailsHeaderName.setHorizontalAlignment(Element.ALIGN_LEFT);
        paymentDetailsHeaderName.setPadding(0.0f);
        paymentDetailsHeaderName.setBorder(Rectangle.NO_BORDER);
        paymentDetailsHeader.addCell(paymentDetailsHeaderName);

        paymentDetailsHeader.setSpacingBefore(0.1F);
        paymentDetailsHeader.setSpacingAfter(1.1F);
        document.add(paymentDetailsHeader);

        /* Populate Payment Details */
        PdfPTable paymentDetailsTable = new PdfPTable(7);
        paymentDetailsTable.setWidthPercentage(70);
        paymentDetailsTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell paymentDetailsTableDateCell = new PdfPCell(new Paragraph(new Chunk("Date", font4)));
        paymentDetailsTableDateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        paymentDetailsTableDateCell.setPadding(5.0f);
        paymentDetailsTableDateCell.setBorder(Rectangle.NO_BORDER);
        paymentDetailsTableDateCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        paymentDetailsTable.addCell(paymentDetailsTableDateCell);

        PdfPCell remittanceq912233333 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        remittanceq912233333.setHorizontalAlignment(Element.ALIGN_LEFT);
        remittanceq912233333.setPadding(5.0f);

        remittanceq912233333.setBorder(Rectangle.NO_BORDER);
        paymentDetailsTable.addCell(remittanceq912233333);

        PdfPCell paymentDetailsTableReceiptCell = new PdfPCell(new Paragraph(new Chunk("Receipt\r\n" + "Number", font4)));
        paymentDetailsTableReceiptCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        paymentDetailsTableReceiptCell.setPadding(5.0f);
        paymentDetailsTableReceiptCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        paymentDetailsTableReceiptCell.setBorder(Rectangle.NO_BORDER);
        paymentDetailsTable.addCell(paymentDetailsTableReceiptCell);

        PdfPCell remittanceqw916633333 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        remittanceqw916633333.setHorizontalAlignment(Element.ALIGN_LEFT);
        remittanceqw916633333.setPadding(5.0f);

        remittanceqw916633333.setBorder(Rectangle.NO_BORDER);
        paymentDetailsTable.addCell(remittanceqw916633333);

        PdfPCell paymentDetailsTableModeOfPaymentCell = new PdfPCell(new Paragraph(new Chunk("Mode Of\r\n" + "Payment", font4)));
        paymentDetailsTableModeOfPaymentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        paymentDetailsTableModeOfPaymentCell.setPadding(5.0f);
        paymentDetailsTableModeOfPaymentCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        paymentDetailsTableModeOfPaymentCell.setBorder(Rectangle.NO_BORDER);
        paymentDetailsTable.addCell(paymentDetailsTableModeOfPaymentCell);

        PdfPCell remittanceqww9533233 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        remittanceqww9533233.setHorizontalAlignment(Element.ALIGN_LEFT);
        remittanceqww9533233.setPadding(5.0f);

        remittanceqww9533233.setBorder(Rectangle.NO_BORDER);
        paymentDetailsTable.addCell(remittanceqww9533233);

        PdfPCell paymentDetailsTableAmountPaidCell = new PdfPCell(new Paragraph(new Chunk("Amount Paid\r\n" + "INR", font4)));
        paymentDetailsTableAmountPaidCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        paymentDetailsTableAmountPaidCell.setPadding(5.0f);
        paymentDetailsTableAmountPaidCell.setBorder(Rectangle.NO_BORDER);
        paymentDetailsTableAmountPaidCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        paymentDetailsTable.addCell(paymentDetailsTableAmountPaidCell);

        float[] columnWidths117534559 = {10.0F, 0.2F, 10.0F, 0.2F, 10.0F, 0.2F, 10.0F};
        paymentDetailsTable.setWidths(columnWidths117534559);
        paymentDetailsTable.setSpacingBefore(2.1F);
        paymentDetailsTable.setSpacingAfter(1.1F);

        document.add(paymentDetailsTable);

        /* Populating data into Payment Details Table */
        int noOfPaymentsDone = invoice.getInvoiceDetails().getPaymentDetails().size();
        for (int k = 0; k < noOfPaymentsDone; k++) {
            PdfPTable paymentDetails = new PdfPTable(7);
            paymentDetails.setWidthPercentage(70);
            paymentDetails.setHorizontalAlignment(Element.ALIGN_LEFT);
            String PaymentDate = invoice.getInvoiceDetails().getPaymentDetails().get(k).getPaymentDate();
            PdfPCell paymentDetailsDateCell = new PdfPCell(new Paragraph(new Chunk(PaymentDate + "", normalTextFont)));
            paymentDetailsDateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            paymentDetailsDateCell.setPadding(0.0f);
            paymentDetailsDateCell.setBorder(Rectangle.NO_BORDER);

            paymentDetails.addCell(paymentDetailsDateCell);

            PdfPCell remittanceq91223334344 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
            remittanceq91223334344.setHorizontalAlignment(Element.ALIGN_LEFT);
            remittanceq91223334344.setPadding(0.0f);

            remittanceq91223334344.setBorder(Rectangle.NO_BORDER);
            paymentDetails.addCell(remittanceq91223334344);

            String receiptNumber = invoice.getInvoiceDetails().getPaymentDetails().get(k).getReceiptNumber();
            PdfPCell paymentDetailsReceiptCell = new PdfPCell(new Paragraph(new Chunk(receiptNumber + "", normalTextFont)));
            paymentDetailsReceiptCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            paymentDetailsReceiptCell.setPadding(0.0f);

            paymentDetailsReceiptCell.setBorder(Rectangle.NO_BORDER);
            paymentDetails.addCell(paymentDetailsReceiptCell);

            PdfPCell remittanceqw91663334434 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
            remittanceqw91663334434.setHorizontalAlignment(Element.ALIGN_LEFT);
            remittanceqw91663334434.setPadding(0.0f);

            remittanceqw91663334434.setBorder(Rectangle.NO_BORDER);
            paymentDetails.addCell(remittanceqw91663334434);

            String modeOfPayment = invoice.getInvoiceDetails().getPaymentDetails().get(k).getPaymentMethod();
            PdfPCell paymentDetailsModeOfPaymentCell = new PdfPCell(new Paragraph(new Chunk(modeOfPayment + "", normalTextFont)));
            paymentDetailsModeOfPaymentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            paymentDetailsModeOfPaymentCell.setPadding(0.0f);

            paymentDetailsModeOfPaymentCell.setBorder(Rectangle.NO_BORDER);
            paymentDetails.addCell(paymentDetailsModeOfPaymentCell);

            PdfPCell remittanceqww95332554 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
            remittanceqww95332554.setHorizontalAlignment(Element.ALIGN_LEFT);
            remittanceqww95332554.setPadding(0.0f);

            remittanceqww95332554.setBorder(Rectangle.NO_BORDER);
            paymentDetails.addCell(remittanceqww95332554);

            double paymentAmount = invoice.getInvoiceDetails().getPaymentDetails().get(k).getPaymentAmount();
            PdfPCell paymentDetailsAmountPaidCell = new PdfPCell(new Paragraph(new Chunk(paymentAmount + "", normalTextFont)));
            paymentDetailsAmountPaidCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            paymentDetailsAmountPaidCell.setPadding(0.0f);
            paymentDetailsAmountPaidCell.setBorder(Rectangle.NO_BORDER);

            paymentDetails.addCell(paymentDetailsAmountPaidCell);

            float[] columnWidths117536449 = {10.0F, 0.2F, 10.0F, 0.2F, 10.0F, 0.2F, 10.0F};
            paymentDetails.setWidths(columnWidths117536449);
            paymentDetails.setSpacingBefore(4.1F);
            document.add(paymentDetails);
        }

        document.close();
        file.close();
        message = "PDF Created successfully and could not send mail as no mail id is given;" + filePath;
        logger.info("PDFService :: generatePDF : Emailds ::"+invoice.getInvoiceDetails().getCustomerDetails().getCustomerEmail());

        try {
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

    private void drawCostsAndDiscounts(Document document, Font normalTextFont, Font font4, Double amount, String string ) throws DocumentException {
        DecimalFormat df2 = new DecimalFormat("####0.00");
        PdfPTable balanceAmountTable = new PdfPTable(11);
        balanceAmountTable.setWidthPercentage(100);
        balanceAmountTable.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell balanceAmountTableCell1 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        balanceAmountTableCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        balanceAmountTableCell1.setPadding(0.0f);
        balanceAmountTableCell1.setBorder(Rectangle.NO_BORDER);
        balanceAmountTable.addCell(balanceAmountTableCell1);

        PdfPCell balanceAmountTableCell2 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        balanceAmountTableCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        balanceAmountTableCell2.setPadding(0.0f);

        balanceAmountTableCell2.setBorder(Rectangle.NO_BORDER);
        balanceAmountTable.addCell(balanceAmountTableCell2);

        PdfPCell balanceAmountTableCell3 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        balanceAmountTableCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
        balanceAmountTableCell3.setPadding(0.0f);

        balanceAmountTableCell3.setBorder(Rectangle.NO_BORDER);
        balanceAmountTable.addCell(balanceAmountTableCell3);

        PdfPCell balanceAmountTableCell4 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        balanceAmountTableCell4.setHorizontalAlignment(Element.ALIGN_LEFT);
        balanceAmountTableCell4.setPadding(0.0f);

        balanceAmountTableCell4.setBorder(Rectangle.NO_BORDER);
        balanceAmountTable.addCell(balanceAmountTableCell4);

        PdfPCell balanceAmountTableCell5 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        balanceAmountTableCell5.setHorizontalAlignment(Element.ALIGN_LEFT);
        balanceAmountTableCell5.setPadding(0.0f);

        balanceAmountTableCell5.setBorder(Rectangle.NO_BORDER);
        balanceAmountTable.addCell(balanceAmountTableCell5);

        PdfPCell balanceAmountTableCell6 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        balanceAmountTableCell6.setHorizontalAlignment(Element.ALIGN_LEFT);
        balanceAmountTableCell6.setPadding(0.0f);

        balanceAmountTableCell6.setBorder(Rectangle.NO_BORDER);
        balanceAmountTable.addCell(balanceAmountTableCell6);

        PdfPCell balanceAmountTableCell7 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        balanceAmountTableCell7.setHorizontalAlignment(Element.ALIGN_LEFT);
        balanceAmountTableCell7.setPadding(0.0f);
        balanceAmountTableCell7.setBorder(Rectangle.NO_BORDER);
        balanceAmountTable.addCell(balanceAmountTableCell7);


        PdfPCell balanceAmountTableCell8 = new PdfPCell(new Paragraph(new Chunk("", font4)));
        balanceAmountTableCell8.setHorizontalAlignment(Element.ALIGN_CENTER);
        balanceAmountTableCell8.setPadding(0.0f);
        balanceAmountTableCell8.setBorder(Rectangle.NO_BORDER);
        balanceAmountTable.addCell(balanceAmountTableCell8);

        PdfPCell balanceAmountTableCell9 = new PdfPCell(new Paragraph(new Chunk(string + " :", normalTextFont)));
        balanceAmountTableCell9.setHorizontalAlignment(Element.ALIGN_RIGHT);
        balanceAmountTableCell9.setPadding(0.0f);
        balanceAmountTableCell9.setBorder(Rectangle.NO_BORDER);
        balanceAmountTable.addCell(balanceAmountTableCell9);


        PdfPCell balanceAmountTableCell10 = new PdfPCell(new Paragraph(new Chunk("", font4)));
        balanceAmountTableCell10.setHorizontalAlignment(Element.ALIGN_CENTER);
        balanceAmountTableCell10.setPadding(0.0f);
        balanceAmountTableCell10.setBorder(Rectangle.NO_BORDER);
        balanceAmountTable.addCell(balanceAmountTableCell10);

        //Double balanceAmount = invoice.getInvoiceDetails().getInvoiceBalanceAmount();
        PdfPCell balanceAmountTableCell11 = new PdfPCell(new Paragraph(new Chunk(df2.format(amount) + " INR", normalTextFont)));
        balanceAmountTableCell11.setHorizontalAlignment(Element.ALIGN_RIGHT);
        balanceAmountTableCell11.setPadding(0.0f);
        balanceAmountTableCell11.setBorder(Rectangle.NO_BORDER);
        balanceAmountTable.addCell(balanceAmountTableCell11);


        float[] columnWidths115579 = {  10.0F, 1.0F,10.0F,1.0F, 10.0F,1.0F, 20.0F,3.0F,10.0F,1.0F,10.0F };
        balanceAmountTable.setWidths(columnWidths115579);
        balanceAmountTable.setSpacingBefore(4.1F);
        balanceAmountTable.setSpacingAfter(5.1F);
        document.add(balanceAmountTable);

    }

    private void drawHorizontalLine(Document document, Font normalTextFont, Font font4) throws DocumentException {
        /* Populate Horizontal Bar */
        PdfPTable remittanceee12 = new PdfPTable(11);
        remittanceee12.setWidthPercentage(100);
        remittanceee12.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell remittance911133 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        remittance911133.setHorizontalAlignment(Element.ALIGN_LEFT);
        remittance911133.setPadding(0.0f);
        remittance911133.setBorder(Rectangle.NO_BORDER);
        remittanceee12.addCell(remittance911133);

        PdfPCell remittanceq912233 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        remittanceq912233.setHorizontalAlignment(Element.ALIGN_LEFT);
        remittanceq912233.setPadding(0.0f);

        remittanceq912233.setBorder(Rectangle.NO_BORDER);
        remittanceee12.addCell(remittanceq912233);

        PdfPCell remittance1193323 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        remittance1193323.setHorizontalAlignment(Element.ALIGN_LEFT);
        remittance1193323.setPadding(0.0f);

        remittance1193323.setBorder(Rectangle.NO_BORDER);
        remittanceee12.addCell(remittance1193323);

        PdfPCell remittanceqw91663 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        remittanceqw91663.setHorizontalAlignment(Element.ALIGN_LEFT);
        remittanceqw91663.setPadding(0.0f);

        remittanceqw91663.setBorder(Rectangle.NO_BORDER);
        remittanceee12.addCell(remittanceqw91663);

        PdfPCell remittance111473 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        remittance111473.setHorizontalAlignment(Element.ALIGN_LEFT);
        remittance111473.setPadding(0.0f);

        remittance111473.setBorder(Rectangle.NO_BORDER);
        remittanceee12.addCell(remittance111473);

        PdfPCell remittanceqww9533 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        remittanceqww9533.setHorizontalAlignment(Element.ALIGN_LEFT);
        remittanceqww9533.setPadding(0.0f);

        remittanceqww9533.setBorder(Rectangle.NO_BORDER);
        remittanceee12.addCell(remittanceqww9533);

        PdfPCell remittance13454 = new PdfPCell(new Paragraph(new Chunk("", normalTextFont)));
        remittance13454.setHorizontalAlignment(Element.ALIGN_LEFT);
        remittance13454.setPadding(0.0f);
        remittance13454.setBorder(Rectangle.NO_BORDER);
        remittanceee12.addCell(remittance13454);

        PdfPCell remittagnce113 = new PdfPCell(new Paragraph(new Chunk("", font4)));
        remittagnce113.setHorizontalAlignment(Element.ALIGN_CENTER);
        remittagnce113.setPadding(0.0f);
        remittagnce113.setBorder(Rectangle.NO_BORDER);
        remittanceee12.addCell(remittagnce113);

        PdfPCell remitta5nceg124 = new PdfPCell(new Paragraph(new Chunk("").setUnderline(0.1f, -2f)));
        remitta5nceg124.setHorizontalAlignment(Element.ALIGN_RIGHT);
        remitta5nceg124.setPadding(0.0f);
        remitta5nceg124.setBorder(Rectangle.BOTTOM);
        remittanceee12.addCell(remitta5nceg124);

        PdfPCell remittancet1123 = new PdfPCell(new Paragraph(new Chunk("", font4)));
        remittancet1123.setHorizontalAlignment(Element.ALIGN_CENTER);
        remittancet1123.setPadding(0.0f);
        remittancet1123.setBorder(Rectangle.NO_BORDER);
        remittanceee12.addCell(remittancet1123);

        PdfPCell remittancergf134 = new PdfPCell(new Paragraph(new Chunk("").setUnderline(0.1f, -2f)));
        remittancergf134.setHorizontalAlignment(Element.ALIGN_RIGHT);
        remittancergf134.setPadding(0.0f);
        remittancergf134.setBorder(Rectangle.BOTTOM);
        remittanceee12.addCell(remittancergf134);

        float[] columnWidths117539 = {  10.0F, 1.0F,10.0F,1.0F, 10.0F,1.0F, 20.0F,3.0F,10.0F,1.0F,10.0F};
        remittanceee12.setWidths(columnWidths117539);
        document.add(remittanceee12);
    }



}
