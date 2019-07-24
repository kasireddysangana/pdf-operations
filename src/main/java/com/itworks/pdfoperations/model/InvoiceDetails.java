package com.itworks.pdfoperations.model;

import java.util.ArrayList;

public class InvoiceDetails {
 private float invoiceBalanceAmount;
 private CustomerDetails customerDetails;
 private LocationDetails locationDetails;
 ArrayList< PaymentDetails > paymentDetails = new ArrayList < PaymentDetails > ();
 private float totalAmountPaid;
 private float totalDiscountAmount;
 private float totalInvoiceAmount;

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
          '}';
 }

 // Getter Methods


 public float getInvoiceBalanceAmount() {
  return invoiceBalanceAmount;
 }

 public void setInvoiceBalanceAmount(float invoiceBalanceAmount) {
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

 public float getTotalAmountPaid() {
  return totalAmountPaid;
 }

 public void setTotalAmountPaid(float totalAmountPaid) {
  this.totalAmountPaid = totalAmountPaid;
 }

 public float getTotalDiscountAmount() {
  return totalDiscountAmount;
 }

 public void setTotalDiscountAmount(float totalDiscountAmount) {
  this.totalDiscountAmount = totalDiscountAmount;
 }

 public float getTotalInvoiceAmount() {
  return totalInvoiceAmount;
 }

 public void setTotalInvoiceAmount(float totalInvoiceAmount) {
  this.totalInvoiceAmount = totalInvoiceAmount;
 }
}