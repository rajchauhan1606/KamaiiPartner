package com.kamaii.partner.ui.models;

import java.io.Serializable;

public class AddressModel implements Serializable {
    String id;
    String cat_id="";
    String status="";

    public AddressModel(String id, String cat_id, String status) {
        this.id = id;
        this.cat_id = cat_id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getStatus() {
        return status;
    }
}
