package com.kamaii.partner.ui.models;

public class GridModel {
    String url;
    String name;
    public GridModel(String name, String url) {
        this.url = url;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}

