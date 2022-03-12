package com.kamaii.partner.ui.models;

public class BusinessCardSliderModel {

    private String img_path;
    private String atist_name = "";
    private String business_name = "";

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getAtist_name() {
        return atist_name;
    }

    public void setAtist_name(String atist_name) {
        this.atist_name = atist_name;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}
