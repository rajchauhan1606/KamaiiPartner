package com.kamaii.partner.DTO;

import java.io.Serializable;

public class TrackingData implements Serializable
{
    double vendorLatitude;
    double vendorLongitude;
    String sourceAddress;
    String destinationAddress;
    String vendorName;
    int artistId;
    double totalAmount;
    String productName;
    String vendorImage;
    String mobileNumber ;
    String screenFlag;
    int bookingFlag ;
    int bookingId;
    int paymentType;
    String bookingDate;
    String bookingTime;
    String vehicleNumber="";

    public double getVendorLatitude() {
        return vendorLatitude;
    }

    public void setVendorLatitude(double vendorLatitude) {
        this.vendorLatitude = vendorLatitude;
    }

    public double getVendorLongitude() {
        return vendorLongitude;
    }

    public void setVendorLongitude(double vendorLongitude) {
        this.vendorLongitude = vendorLongitude;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVendorImage() {
        return vendorImage;
    }

    public void setVendorImage(String vendorImage) {
        this.vendorImage = vendorImage;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getScreenFlag() {
        return screenFlag;
    }

    public void setScreenFlag(String screenFlag) {
        this.screenFlag = screenFlag;
    }

    public int getBookingFlag() {
        return bookingFlag;
    }

    public void setBookingFlag(int bookingFlag) {
        this.bookingFlag = bookingFlag;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
