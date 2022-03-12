

package com.kamaii.partner.ui.models.newbooking;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class NewOrderDetailsModel {

        @SerializedName("orderflag")
        @Expose
        private String orderflag;
        @SerializedName("orderid")
        @Expose
        private String orderid;
        @SerializedName("ProductsArray")
        @Expose
        private List<ProductsArray> productsArray = null;
        @SerializedName("totalamount")
        @Expose
        private String totalamount;
        @SerializedName("orderdatetime")
        @Expose
        private String orderdatetime;
        @SerializedName("deliverydatetime")
        @Expose
        private String deliverydatetime;
        @SerializedName("checkthings")
        @Expose
        private String checkthings;
        private String msg = "";

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getOrderflag() {
            return orderflag;
        }

        public void setOrderflag(String orderflag) {
            this.orderflag = orderflag;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public List<ProductsArray> getProductsArray() {
            return productsArray;
        }

        public void setProductsArray(List<ProductsArray> productsArray) {
            this.productsArray = productsArray;
        }

        public String getTotalamount() {
            return totalamount;
        }

        public void setTotalamount(String totalamount) {
            this.totalamount = totalamount;
        }

        public String getOrderdatetime() {
            return orderdatetime;
        }

        public void setOrderdatetime(String orderdatetime) {
            this.orderdatetime = orderdatetime;
        }

        public String getDeliverydatetime() {
            return deliverydatetime;
        }

        public void setDeliverydatetime(String deliverydatetime) {
            this.deliverydatetime = deliverydatetime;
        }

        public String getCheckthings() {
            return checkthings;
        }

        public void setCheckthings(String checkthings) {
            this.checkthings = checkthings;
        }

    }


