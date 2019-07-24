package com.itworks.pdfoperations.model;

public class ServiceRequestDetails {
    private double amountPaid;
    private double balanceAmount;
    private String doctorName;
    private String serviceName;
    private String serviceNameColor;
    private String serviceRequestDate;
    private double totalAmount;
    private double totalDiscount;

    @Override
    public String toString() {
        return "ServiceRequestDetails{" +
                "amountPaid=" + amountPaid +
                ", balanceAmount=" + balanceAmount +
                ", doctorName='" + doctorName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceNameColor='" + serviceNameColor + '\'' +
                ", serviceRequestDate='" + serviceRequestDate + '\'' +
                ", totalAmount=" + totalAmount +
                ", totalDiscount=" + totalDiscount +
                '}';
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getAmountpaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountpaid) {
        this.amountPaid = amountpaid;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        this.balanceAmount = balanceAmount;
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

    public String getServiceNameColor() {
        return serviceNameColor;
    }

    public void setServiceNameColor(String serviceNameColor) {
        this.serviceNameColor = serviceNameColor;
    }

    public String getServiceRequestDate() {
        return serviceRequestDate;
    }

    public void setServiceRequestDate(String serviceRequestDate) {
        this.serviceRequestDate = serviceRequestDate;
    }
}
