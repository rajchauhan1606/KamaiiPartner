package com.kamaii.partner.ui.models;

import java.io.Serializable;


public class ReferCustomer implements Serializable {

    String user_id = "";
    String name = "";
    String mobile = "";
    String email_id = "";
    String image = "";
    String cus_id  = "";


    public String getCus_id() {
        return cus_id;
    }

    public void setCus_id(String cus_id) {
        this.cus_id = cus_id;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getImage() {
        return image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
