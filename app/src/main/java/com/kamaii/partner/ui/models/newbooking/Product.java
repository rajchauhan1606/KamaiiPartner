
package com.kamaii.partner.ui.models.newbooking; ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("order_amount")
    @Expose
    private String orderAmount;
    @SerializedName("prepration_time")
    @Expose
    private String preprationTime;
    @SerializedName("delivery_time")
    @Expose
    private String deliveryTime;

    String order_no2="";
    String service_id ="";
    String slot = "";
    String lunch_dinner = "";

    public String getOrder_no2() {
        return order_no2;
    }

    public void setOrder_no2(String order_no2) {
        this.order_no2 = order_no2;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getLunch_dinner() {
        return lunch_dinner;
    }

    public void setLunch_dinner(String lunch_dinner) {
        this.lunch_dinner = lunch_dinner;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getPreprationTime() {
        return preprationTime;
    }

    public void setPreprationTime(String preprationTime) {
        this.preprationTime = preprationTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

}
