package com.kamaii.partner.ui.models;

import java.io.Serializable;

public class customerservicemodel implements Serializable {

    String id = "";
    String product_name = "";
    String name = "";
    String booking_date = "";


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }
}
