package com.itworks.pdfoperations.pojo;

public class Response {
    private int responseCode;
    private String responseMessage;
    private String pdfPath;

    public Response() {
    }

    public Response(int responseCode, String responseMessage, String pdfPath) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.pdfPath = pdfPath;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
}
