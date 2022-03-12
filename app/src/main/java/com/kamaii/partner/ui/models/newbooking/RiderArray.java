
package com.kamaii.partner.ui.models.newbooking;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RiderArray {

    @SerializedName("rider_name")
    @Expose
    private String riderName;
    @SerializedName("rider_mobile_no")
    @Expose
    private String riderMobileNo;
    @SerializedName("rider_id")
    @Expose
    private String riderId;
    @SerializedName("rider_arrvival_msg")
    @Expose
    private String riderArrvivalMsg;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderMobileNo() {
        return riderMobileNo;
    }

    public void setRiderMobileNo(String riderMobileNo) {
        this.riderMobileNo = riderMobileNo;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getRiderArrvivalMsg() {
        return riderArrvivalMsg;
    }

    public void setRiderArrvivalMsg(String riderArrvivalMsg) {
        this.riderArrvivalMsg = riderArrvivalMsg;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
