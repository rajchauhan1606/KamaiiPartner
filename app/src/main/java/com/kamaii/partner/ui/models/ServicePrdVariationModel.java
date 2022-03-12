package com.kamaii.partner.ui.models;

public class ServicePrdVariationModel {

    String price = "";
    String minqty = "";
    String maxqty = "";
    String type = "";
    String typename = "";

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMinqty() {
        return minqty;
    }

    public void setMinqty(String minqty) {
        this.minqty = minqty;
    }

    public String getMaxqty() {
        return maxqty;
    }

    public void setMaxqty(String maxqty) {
        this.maxqty = maxqty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
