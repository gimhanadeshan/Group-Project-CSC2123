package com.example.wildlife_hms;

public class CompanyDetailsModel {

    private final int id=1;
    private String name;
    private String address;
    private String email;
    private String telNo;
    private String logo;

    public CompanyDetailsModel(String name, String address, String email, String telNo, String logo) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.telNo = telNo;
        this.logo = logo;
    }

    public CompanyDetailsModel(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
