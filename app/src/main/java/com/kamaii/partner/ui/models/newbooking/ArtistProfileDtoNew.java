package com.kamaii.partner.ui.models.newbooking;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ArtistProfileDtoNew implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("download_agreement")
    @Expose
    private String downloadAgreement;
    @SerializedName("house_no")
    @Expose
    private String houseNo;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("stretaddress")
    @Expose
    private String stretaddress;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("addhar_card")
    @Expose
    private String addharCard;
    @SerializedName("addhar_card_back")
    @Expose
    private String addharCardBack;
    @SerializedName("pan_card")
    @Expose
    private String panCard;
    @SerializedName("cheque")
    @Expose
    private String cheque;
    private String tracker;
    private String user_bank_name;
    private String bank_name;
    private String ifsc_code;
    private String account_number;
    private String check_bank_tracker;

    public String getCheck_bank_tracker() {
        return check_bank_tracker;
    }

    public void setCheck_bank_tracker(String check_bank_tracker) {
        this.check_bank_tracker = check_bank_tracker;
    }

    public String getUser_bank_name() {
        return user_bank_name;
    }

    public void setUser_bank_name(String user_bank_name) {
        this.user_bank_name = user_bank_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getTracker() {
        return tracker;
    }

    public void setTracker(String tracker) {
        this.tracker = tracker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDownloadAgreement() {
        return downloadAgreement;
    }

    public void setDownloadAgreement(String downloadAgreement) {
        this.downloadAgreement = downloadAgreement;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getStretaddress() {
        return stretaddress;
    }

    public void setStretaddress(String stretaddress) {
        this.stretaddress = stretaddress;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAddharCard() {
        return addharCard;
    }

    public void setAddharCard(String addharCard) {
        this.addharCard = addharCard;
    }

    public String getAddharCardBack() {
        return addharCardBack;
    }

    public void setAddharCardBack(String addharCardBack) {
        this.addharCardBack = addharCardBack;
    }

    public String getPanCard() {
        return panCard;
    }

    public void setPanCard(String panCard) {
        this.panCard = panCard;
    }

    public String getCheque() {
        return cheque;
    }

    public void setCheque(String cheque) {
        this.cheque = cheque;
    }
}
