package com.kamaii.partner.ui.models;

import java.io.Serializable;
import java.util.ArrayList;


public class ReferServiceModel implements Serializable {

    String image = "";
    String name = "";
    String mobile = "";
    String email_id = "";
    String total_use_service = "";
    String total_commision = "";
    ArrayList<customerservicemodel> ser_list = new ArrayList<>();

    public String getImage() {
        return image;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getTotal_use_service() {
        return total_use_service;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTotal_use_service(String total_use_service) {
        this.total_use_service = total_use_service;
    }

    public String getTotal_commision() {
        return total_commision;
    }

    public void setTotal_commision(String total_commision) {
        this.total_commision = total_commision;
    }

    public ArrayList<customerservicemodel> getSer_list() {
        return ser_list;
    }

    public void setSer_list(ArrayList<customerservicemodel> ser_list) {
        this.ser_list = ser_list;
    }
}
