package com.kamaii.partner.ui.models.newbooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {

    @SerializedName("orderid")
    @Expose
    private String orderid;
    @SerializedName("products_array")
    @Expose
    private List<ProductsArray> products = null;
    @SerializedName("totpayment")
    @Expose
    private Integer totpayment;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public List<ProductsArray> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsArray> products) {
        this.products = products;
    }

    public Integer getTotpayment() {
        return totpayment;
    }

    public void setTotpayment(Integer totpayment) {
        this.totpayment = totpayment;
    }
}
