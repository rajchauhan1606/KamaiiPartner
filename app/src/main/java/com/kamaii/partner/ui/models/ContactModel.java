package com.kamaii.partner.ui.models;

public class ContactModel {

    int Id;
    String name;
    String phone_number;
    int favourite;
    int alreadysent;

    public ContactModel() {
    }

    public ContactModel(int id,String name, String phone_number, int favourite, int alreadysent) {
        Id=id;
        this.name = name;
        this.phone_number = phone_number;
        this.favourite= favourite;
        this.alreadysent = alreadysent;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public int getAlreadysent() {
        return alreadysent;
    }

    public void setAlreadysent(int alreadysent) {
        this.alreadysent = alreadysent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
