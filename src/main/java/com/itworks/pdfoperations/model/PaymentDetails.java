package com.itworks.pdfoperations.model;

import java.util.Date;

public class PaymentDetails {

    private double paymentAmount;
    private String paymentDate;
    private String paymentMethod;

    @Override
    public String toString() {
        return "PaymentDetails{" +
                "paymentAmount=" + paymentAmount +
                ", paymentDate='" + paymentDate + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }


    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
