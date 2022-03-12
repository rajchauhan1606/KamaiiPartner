
package com.kamaii.partner.ui.models.newbooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductsArray {

    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("tprice")
    @Expose
    private String tprice;
    @SerializedName("sprice")
    @Expose
    private String sprice;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("id")
    @Expose
    private String id;

    String cooking_instruction = "";

    public String getCooking_instruction() {
        return cooking_instruction;
    }

    public void setCooking_instruction(String cooking_instruction) {
        this.cooking_instruction = cooking_instruction;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTprice() {
        return tprice;
    }

    public void setTprice(String tprice) {
        this.tprice = tprice;
    }

    public String getSprice() {
        return sprice;
    }

    public void setSprice(String sprice) {
        this.sprice = sprice;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
