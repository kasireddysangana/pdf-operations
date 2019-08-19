package com.itworks.pdfoperations.model;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class CustomerDetails {
 @NotNull(message = "Customer Age can not be blank")
 private String customerAge;
 @NotNull(message = "Customer Gender can not be blank")
 private String customerGender;
 @NotNull(message = "Customer Id can not be blank")
 private int customerId;
 @NotNull(message = "Customer Name can not be blank")
 private String customerName;
 private String customerAddress;
 @NotNull(message = "Customer Phone can not be blank")
 private String customerPhoneNumber;
 @NotNull(message = "Customer Email can not be blank")
 private String customerEmail;

 private List< ServiceRequestDetails > serviceRequestDetails = new ArrayList <> ();

 public List<ServiceRequestDetails> getServiceRequestDetails() {
  return serviceRequestDetails;
 }


 @Override
 public String toString() {
  return "CustomerDetails{" +
          "customerAge='" + customerAge + '\'' +
          ", customerGender='" + customerGender + '\'' +
          ", customerId=" + customerId +
          ", customerName='" + customerName + '\'' +
          ", customerAddress='" + customerAddress + '\'' +
          ", customerPhoneNumber='" + customerPhoneNumber + '\'' +
          ", customerEmail='" + customerEmail + '\'' +
          ", serviceRequestDetails=" + serviceRequestDetails +
          '}';
 }


 // Getter Methods


 public String getCustomerAddress() {
  return customerAddress;
 }

 public void setCustomerAddress(String customerAddress) {
  this.customerAddress = customerAddress;
 }

 public String getCustomerEmail() {
  return customerEmail;
 }

 public void setCustomerEmail(String customerEmail) {
  this.customerEmail = customerEmail;
 }

 public String getCustomerAge() {
  return customerAge;
 }

 public String getCustomerGender() {
  return customerGender;
 }

 public int getCustomerId() {
  return customerId;
 }

 public String getCustomerName() {
  return customerName;
 }

 public String getCustomerPhoneNumber() {
  return customerPhoneNumber;
 }

 // Setter Methods 

 public void setCustomerAge(String customerAge) {
  this.customerAge = customerAge;
 }

 public void setCustomerGender(String customerGender) {
  this.customerGender = customerGender;
 }

 public void setCustomerId(int customerId) {
  this.customerId = customerId;
 }

 public void setCustomerName(String customerName) {
  this.customerName = customerName;
 }

 public void setCustomerPhoneNumber(String customerPhoneNumber) {
  this.customerPhoneNumber = customerPhoneNumber;
 }
}