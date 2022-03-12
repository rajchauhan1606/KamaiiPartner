package com.kamaii.partner.ui.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HomePageModel {

    @SerializedName("catelog_msg")
    @Expose
    private String catelogMsg;
    @SerializedName("catelog")
    @Expose
    private ArrayList<SiderModel> catelog = null;
    @SerializedName("cat_image")
    @Expose
    private String catImage;
    @SerializedName("total_cat")
    @Expose
    private String totalCat;

    @SerializedName("yt_image")
    @Expose
    private String youtubeimage;
    @SerializedName("refer_img")
    @Expose
    private String refer_img;
    @SerializedName("cat_image")
    @Expose
    private String cat_image;
    @SerializedName("share_profile")
    @Expose
    private String share_profile;


    @SerializedName("total_item")
    @Expose
    private String totalItem;
    @SerializedName("total_visit_profile")
    @Expose
    private String totalVisitProfile;
    @SerializedName("item_visit")
    @Expose
    private String itemVisit;

@SerializedName("yt_image_link")
    @Expose
    private String ytimagelink;

@SerializedName("share_profile_link")
    @Expose
    private String shareprofilelink;

@SerializedName("refer_img_link")
    @Expose
    private String referimglink;

@SerializedName("catimagelink")
    @Expose
    private String catimagelink;


    private String today_earning;
    private String total_earning;
    private String request_amount;
    private String kamaii_wallet;
    private String online_services;
    private String today_orders;
    private String pending_orders;
    private String completed_orders;


    public String getToday_earning() {
        return today_earning;
    }

    public void setToday_earning(String today_earning) {
        this.today_earning = today_earning;
    }

    public String getTotal_earning() {
        return total_earning;
    }

    public void setTotal_earning(String total_earning) {
        this.total_earning = total_earning;
    }

    public String getRequest_amount() {
        return request_amount;
    }

    public void setRequest_amount(String request_amount) {
        this.request_amount = request_amount;
    }

    public String getKamaii_wallet() {
        return kamaii_wallet;
    }

    public void setKamaii_wallet(String kamaii_wallet) {
        this.kamaii_wallet = kamaii_wallet;
    }

    public String getOnline_services() {
        return online_services;
    }

    public void setOnline_services(String online_services) {
        this.online_services = online_services;
    }

    public String getToday_orders() {
        return today_orders;
    }

    public void setToday_orders(String today_orders) {
        this.today_orders = today_orders;
    }

    public String getPending_orders() {
        return pending_orders;
    }

    public void setPending_orders(String pending_orders) {
        this.pending_orders = pending_orders;
    }

    public String getCompleted_orders() {
        return completed_orders;
    }

    public void setCompleted_orders(String completed_orders) {
        this.completed_orders = completed_orders;
    }

    public String getYtimagelink() {
        return ytimagelink;
    }

    public void setYtimagelink(String ytimagelink) {
        this.ytimagelink = ytimagelink;
    }

    public String getShareprofilelink() {
        return shareprofilelink;
    }

    public void setShareprofilelink(String shareprofilelink) {
        this.shareprofilelink = shareprofilelink;
    }

    public String getReferimglink() {
        return referimglink;
    }

    public void setReferimglink(String referimglink) {
        this.referimglink = referimglink;
    }

    public String getCatimagelink() {
        return catimagelink;
    }

    public void setCatimagelink(String catimagelink) {
        this.catimagelink = catimagelink;
    }

    public String getYoutubeimage() {
        return youtubeimage;
    }

    public void setYoutubeimage(String youtubeimage) {
        this.youtubeimage = youtubeimage;
    }

    public String getRefer_img() {
        return refer_img;
    }

    public void setRefer_img(String refer_img) {
        this.refer_img = refer_img;
    }

    public String getCat_image() {
        return cat_image;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }

    public String getShare_profile() {
        return share_profile;
    }

    public void setShare_profile(String share_profile) {
        this.share_profile = share_profile;
    }

    public String getCatelogMsg() {
        return catelogMsg;
    }

    public void setCatelogMsg(String catelogMsg) {
        this.catelogMsg = catelogMsg;
    }

    public ArrayList<SiderModel> getCatelog() {
        return catelog;
    }

    public void setCatelog(ArrayList<SiderModel> catelog) {
        this.catelog = catelog;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public String getTotalCat() {
        return totalCat;
    }

    public void setTotalCat(String totalCat) {
        this.totalCat = totalCat;
    }

    public String getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(String totalItem) {
        this.totalItem = totalItem;
    }

    public String getTotalVisitProfile() {
        return totalVisitProfile;
    }

    public void setTotalVisitProfile(String totalVisitProfile) {
        this.totalVisitProfile = totalVisitProfile;
    }

    public String getItemVisit() {
        return itemVisit;
    }

    public void setItemVisit(String itemVisit) {
        this.itemVisit = itemVisit;
    }

}
