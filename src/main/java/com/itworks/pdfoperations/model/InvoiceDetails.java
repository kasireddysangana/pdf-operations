package com.itworks.pdfoperations.model;

import java.util.ArrayList;

public class InvoiceDetails {
 private Double invoiceBalanceAmount;
 private CustomerDetails customerDetails;
 private LocationDetails locationDetails;
 private ArrayList< PaymentDetails > paymentDetails = new ArrayList <> ();
 private Double totalAmountPaid;
 private Double totalDiscountAmount;
 private Double totalInvoiceAmount;
 private String invoiceDate;
 private String invoiceNumber;

 @Override
 public String toString() {
  return "InvoiceDetails{" +
          "invoiceBalanceAmount=" + invoiceBalanceAmount +
          ", customerDetails=" + customerDetails +
          ", locationDetails=" + locationDetails +
          ", paymentDetails=" + paymentDetails +
          ", totalAmountPaid=" + totalAmountPaid +
          ", totalDiscountAmount=" + totalDiscountAmount +
          ", totalInvoiceAmount=" + totalInvoiceAmount +
          ", invoiceDate='" + invoiceDate + '\'' +
          ", invoiceNumber='" + invoiceNumber + '\'' +
          '}';
 }

 // Getter Methods


    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getInvoiceBalanceAmount() {
  return invoiceBalanceAmount;
 }

 public void setInvoiceBalanceAmount(Double invoiceBalanceAmount) {
  this.invoiceBalanceAmount = invoiceBalanceAmount;
 }

 public CustomerDetails getCustomerDetails() {
  return customerDetails;
 }

 public void setCustomerDetails(CustomerDetails customerDetails) {
  this.customerDetails = customerDetails;
 }

 public LocationDetails getLocationDetails() {
  return locationDetails;
 }

 public void setLocationDetails(LocationDetails locationDetails) {
  this.locationDetails = locationDetails;
 }

 public ArrayList<PaymentDetails> getPaymentDetails() {
  return paymentDetails;
 }

 public void setPaymentDetails(ArrayList<PaymentDetails> paymentDetails) {
  this.paymentDetails = paymentDetails;
 }

 public Double getTotalAmountPaid() {
  return totalAmountPaid;
 }

 public void setTotalAmountPaid(Double totalAmountPaid) {
  this.totalAmountPaid = totalAmountPaid;
 }

 public Double getTotalDiscountAmount() {
  return totalDiscountAmount;
 }

 public void setTotalDiscountAmount(Double totalDiscountAmount) {
  this.totalDiscountAmount = totalDiscountAmount;
 }

 public Double getTotalInvoiceAmount() {
  return totalInvoiceAmount;
 }

 public void setTotalInvoiceAmount(Double totalInvoiceAmount) {
  this.totalInvoiceAmount = totalInvoiceAmount;
 }
}