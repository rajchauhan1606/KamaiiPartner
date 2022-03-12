package com.kamaii.partner.model;

import com.kamaii.partner.ui.models.Description;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommonServiceModel implements Serializable {

    String id;
    String service_title;
    String cat_id = "";
    String sub_id = "";
    String third_id = "";
    String service_id = "";
    String service_name = "";
    String price = "";
    String service_charge = "";
    String rate = "";
    //String description = "";
    String quantitydays = "";
    String maxmiumvalue = "";
    String roundtrip = "";
    String image = "";
    String maxprice = "";
    String my_location = "";
    String status = "";
    String created_at = "";
    String updated_at = "";
    boolean isSelected;
    private ArrayList<Description> description = null;


    public ArrayList<Description> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<Description> description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService_title() {
        return service_title;
    }

    public void setService_title(String service_title) {
        this.service_title = service_title;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public String getThird_id() {
        return third_id;
    }

    public void setThird_id(String third_id) {
        this.third_id = third_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getQuantitydays() {
        return quantitydays;
    }

    public void setQuantitydays(String quantitydays) {
        this.quantitydays = quantitydays;
    }

    public String getMaxmiumvalue() {
        return maxmiumvalue;
    }

    public void setMaxmiumvalue(String maxmiumvalue) {
        this.maxmiumvalue = maxmiumvalue;
    }

    public String getRoundtrip() {
        return roundtrip;
    }

    public void setRoundtrip(String roundtrip) {
        this.roundtrip = roundtrip;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(String maxprice) {
        this.maxprice = maxprice;
    }

    public String getMy_location() {
        return my_location;
    }

    public void setMy_location(String my_location) {
        this.my_location = my_location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
