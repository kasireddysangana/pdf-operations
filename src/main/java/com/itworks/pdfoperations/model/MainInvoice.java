package com.itworks.pdfoperations.model;

public class MainInvoice {
 private int invoiceId;
 private InvoiceDetails invoiceDetails;

 @Override
 public String toString() {
  return "MainInvoice{" +
          "invoiceId=" + invoiceId +
          ", invoiceDetails=" + invoiceDetails +
          '}';
 }


 // Getter Methods

 public int getInvoiceId() {
  return invoiceId;
 }

 public void setInvoiceId(int invoiceId) {
  this.invoiceId = invoiceId;
 }

 public InvoiceDetails getInvoiceDetails() {
  return invoiceDetails;
 }

 public void setInvoiceDetails(InvoiceDetails invoiceDetails) {
  this.invoiceDetails = invoiceDetails;
 }
}