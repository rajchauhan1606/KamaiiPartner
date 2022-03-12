
package com.kamaii.partner.ui.models.newbooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TotalOrderModel {

    @SerializedName("pending_data")
    @Expose
    private List<PendingDatum> pendingData = null;
    @SerializedName("tiffin_order_tracker")
    @Expose
    private String tiffinOrderTracker;
    @SerializedName("other_order_tracker")
    @Expose
    private String otherOrderTracker;
    @SerializedName("kono_data")
    @Expose
    private String konoData;
    @SerializedName("drop_down_array")
    @Expose
    private List<Object> dropDownArray = null;
    @SerializedName("todayorder")
    @Expose
    private String todayorder;
    @SerializedName("lunch_order")
    @Expose
    private String lunchOrder;
    @SerializedName("dinner_order")
    @Expose
    private String dinnerOrder;
    @SerializedName("total_order")
    @Expose
    private String totalOrder;
    @SerializedName("lunch_dinner_data")
    @Expose
    private String lunchDinnerData;
    @SerializedName("lunch_or_order")
    @Expose
    private String lunchOrOrder;
    @SerializedName("order_ids")
    @Expose
    private String orderIds;

    public List<PendingDatum> getPendingData() {
        return pendingData;
    }

    public void setPendingData(List<PendingDatum> pendingData) {
        this.pendingData = pendingData;
    }

    public String getTiffinOrderTracker() {
        return tiffinOrderTracker;
    }

    public void setTiffinOrderTracker(String tiffinOrderTracker) {
        this.tiffinOrderTracker = tiffinOrderTracker;
    }

    public String getOtherOrderTracker() {
        return otherOrderTracker;
    }

    public void setOtherOrderTracker(String otherOrderTracker) {
        this.otherOrderTracker = otherOrderTracker;
    }

    public String getKonoData() {
        return konoData;
    }

    public void setKonoData(String konoData) {
        this.konoData = konoData;
    }

    public List<Object> getDropDownArray() {
        return dropDownArray;
    }

    public void setDropDownArray(List<Object> dropDownArray) {
        this.dropDownArray = dropDownArray;
    }

    public String getTodayorder() {
        return todayorder;
    }

    public void setTodayorder(String todayorder) {
        this.todayorder = todayorder;
    }

    public String getLunchOrder() {
        return lunchOrder;
    }

    public void setLunchOrder(String lunchOrder) {
        this.lunchOrder = lunchOrder;
    }

    public String getDinnerOrder() {
        return dinnerOrder;
    }

    public void setDinnerOrder(String dinnerOrder) {
        this.dinnerOrder = dinnerOrder;
    }

    public String getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(String totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getLunchDinnerData() {
        return lunchDinnerData;
    }

    public void setLunchDinnerData(String lunchDinnerData) {
        this.lunchDinnerData = lunchDinnerData;
    }

    public String getLunchOrOrder() {
        return lunchOrOrder;
    }

    public void setLunchOrOrder(String lunchOrOrder) {
        this.lunchOrOrder = lunchOrOrder;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

}
