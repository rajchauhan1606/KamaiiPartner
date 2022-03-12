package com.kamaii.partner.ui.models;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    String id;
    String cat_name="";
    String image = "";
    String is_map = "";
    String kmrange = "";

    public String getKmrange() {
        return kmrange;
    }

    public void setKmrange(String kmrange) {
        this.kmrange = kmrange;
    }

    public String getIs_map() {
        return is_map;
    }

    public void setIs_map(String is_map) {
        this.is_map = is_map;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }


}
