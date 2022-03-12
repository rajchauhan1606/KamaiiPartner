
package com.kamaii.partner.ui.models.newbooking;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewOrders {

    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("products_array")
    @Expose
    private List<ProductsArray> productsArray = null;
    @SerializedName("order_payment")
    @Expose
    private String orderPayment;
    @SerializedName("app_flag")
    @Expose
    private String appFlag;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("rider_data")
    @Expose
    private List<RiderArray> riderArray = null;
    String end_time_tracker = "";
    String rider_name = "";
    String covid_msg = "";
    String end_time_diff = "";

    public String getEnd_time_diff() {
        return end_time_diff;
    }

    public void setEnd_time_diff(String end_time_diff) {
        this.end_time_diff = end_time_diff;
    }

    public String getRider_name() {
        return rider_name;
    }

    public void setRider_name(String rider_name) {
        this.rider_name = rider_name;
    }

    public String getEnd_time_tracker() {
        return end_time_tracker;
    }

    public void setEnd_time_tracker(String end_time_tracker) {
        this.end_time_tracker = end_time_tracker;
    }

    public String getCovid_msg() {
        return covid_msg;
    }

    public void setCovid_msg(String covid_msg) {
        this.covid_msg = covid_msg;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<ProductsArray> getProductsArray() {
        return productsArray;
    }

    public void setProductsArray(List<ProductsArray> productsArray) {
        this.productsArray = productsArray;
    }

    public String getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(String orderPayment) {
        this.orderPayment = orderPayment;
    }

    public String getAppFlag() {
        return appFlag;
    }

    public void setAppFlag(String appFlag) {
        this.appFlag = appFlag;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<RiderArray> getRiderArray() {
        return riderArray;
    }

    public void setRiderArray(List<RiderArray> riderArray) {
        this.riderArray = riderArray;
    }

}
