package com.kamaii.partner.ui.models;

import java.util.List;

public class SlotDataModel {


    String product_name;
    String description;
    String desc_type;
    String product_qty;
    String subproduct = "";
    String subproductcount = "";
    List<SlotSubDataModel> small_desc_number;

    public String getDesc_type() {
        return desc_type;
    }

    public void setDesc_type(String desc_type) {
        this.desc_type = desc_type;
    }

    public List<SlotSubDataModel> getSmall_desc_number() {
        return small_desc_number;
    }

    public void setSmall_desc_number(List<SlotSubDataModel> small_desc_number) {
        this.small_desc_number = small_desc_number;
    }

    public SlotDataModel(String product_name, String product_qty) {
        this.product_name = product_name;
        this.product_qty = product_qty;
    }

    public String getSubproduct() {
        return subproduct;
    }

    public void setSubproduct(String subproduct) {
        this.subproduct = subproduct;
    }

    public String getSubproductcount() {
        return subproductcount;
    }

    public void setSubproductcount(String subproductcount) {
        this.subproductcount = subproductcount;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

  /*  public String getDesc_type() {
        return desc_type;
    }

    public void setDesc_type(String desc_type) {
        this.desc_type = desc_type;
    }
*/
    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }


}
