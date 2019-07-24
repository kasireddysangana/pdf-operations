package com.itworks.pdfoperations.model;

public class LocationDetails {
 private String locationAddress;
 private String locationName;
 private String locationNumber;
 private String locationImagePath;

 @Override
 public String toString() {
  return "LocationDetails{" +
          "locationAddress='" + locationAddress + '\'' +
          ", locationName='" + locationName + '\'' +
          ", locationNumber='" + locationNumber + '\'' +
          ", locationImagePath='" + locationImagePath + '\'' +
          '}';
 }

 public String getLocationAddress() {
  return locationAddress;
 }

 public void setLocationAddress(String locationAddress) {
  this.locationAddress = locationAddress;
 }

 public String getLocationName() {
  return locationName;
 }

 public void setLocationName(String locationName) {
  this.locationName = locationName;
 }

 public String getLocationNumber() {
  return locationNumber;
 }

 public void setLocationNumber(String locationNumber) {
  this.locationNumber = locationNumber;
 }

 public String getLocationImagePath() {
  return locationImagePath;
 }

 public void setLocationImagePath(String locationImagePath) {
  this.locationImagePath = locationImagePath;
 }
}