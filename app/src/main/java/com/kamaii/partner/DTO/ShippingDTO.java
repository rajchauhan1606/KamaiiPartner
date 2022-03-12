package com.kamaii.partner.DTO;

import java.io.Serializable;


public class ShippingDTO implements Serializable {

    String id = "";
    String user_id = "";
    String sub_cat_id = "";
    String shipping = "";
    String my_location = "";
    String rider = "";
    String maxprice = "";


    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(String maxprice) {
        this.maxprice = maxprice;
    }

    public String getId() {
        return id;
    }

    public String getMy_location() {
        return my_location;
    }

    public String getShipping() {
        return shipping;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMy_location(String my_location) {
        this.my_location = my_location;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
