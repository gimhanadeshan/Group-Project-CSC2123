package com.example.wildlife_hms;

import java.sql.Date;

public class UserModel {

    private int userid;
    private String userName;
    private String pwd;
    private Date regDate;
    private String firstName;
    private String lastName;
    private String email;
    private String roll;
    private String gender;
    private String dp;
    private Boolean active;

    public UserModel(String userName) {
        this.userName = userName;
    }

    public UserModel(int userid, String userName, String pwd, Date regDate, String firstName, String lastName, String email, String roll, String gender, String dp, Boolean active) {
        this.userid = userid;
        this.userName = userName;
        this.pwd = pwd;
        this.regDate = regDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roll = roll;
        this.gender = gender;
        this.dp = dp;
        this.active = active;
    }


    public UserModel(int userid, String userName, String pwd, Date regDate, String firstName, String lastName, String email, String roll, String gender, String dp) {
        this.userid = userid;
        this.userName = userName;
        this.pwd = pwd;
        this.regDate = regDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roll = roll;
        this.gender = gender;
        this.dp = dp;
    }


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
