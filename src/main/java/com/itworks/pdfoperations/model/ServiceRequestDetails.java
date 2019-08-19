package com.itworks.pdfoperations.model;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

public class ServiceRequestDetails {
    private double amountPaid;
    private String doctorName;
    private String serviceName;
    private BaseColor serviceNameColor;
    private String serviceRequestDate;
    private double unitCost;
    private int quantity;
    private double discountAmount;

    @Override
    public String toString() {
        return "ServiceRequestDetails{" +
                ", amountPaid=" + amountPaid +
                ", doctorName='" + doctorName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceNameColor='" + serviceNameColor + '\'' +
                ", serviceRequestDate='" + serviceRequestDate + '\'' +
                ", unitCost=" + unitCost +
                ", quantity=" + quantity +
                ", discountAmount=" + discountAmount +
                '}';
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BaseColor getServiceNameColor() {
        return serviceNameColor;
    }

    public void setServiceNameColor(BaseColor serviceNameColor) {
        this.serviceNameColor = serviceNameColor;
    }

    public String getServiceRequestDate() {
        return serviceRequestDate;
    }

    public void setServiceRequestDate(String serviceRequestDate) {
        this.serviceRequestDate = serviceRequestDate;
    }
}
