package com.example.wildlife_hms;


import java.sql.Date;

public class AlertModel {
    private int alertId;

    private int habitatID;
    private  String alertType;
    private  String alertDisc;
    private Date alertDate;

    private  String alId;

    private String haId;

    private String habitatName;


    public AlertModel(int alertId, String alId, String alertType, String alertDisc, Date alertDate,int habitatID) {
        this.alertId = alertId;
        this.alId = alId;
        this.alertType = alertType;
        this.alertDisc = alertDisc;
        this.alertDate = alertDate;
        this.habitatID = habitatID;
    }

    public AlertModel(int alertId, String alId, String alertType, String alertDisc, Date alertDate,int habitatID,String haId,String habitatName) {
        this.alertId = alertId;
        this.alId = alId;
        this.alertType = alertType;
        this.alertDisc = alertDisc;
        this.alertDate = alertDate;
        this.habitatID = habitatID;
        this.haId=haId;
        this.habitatName=habitatName;
    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }



    public int getHabitatID() {
        return habitatID;
    }

    public void setHabitatID(int habitatID) {
        this.habitatID = habitatID;
    }


    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getAlertDisc() {
        return alertDisc;
    }

    public void setAlertDisc(String alertDisc) {
        this.alertDisc = alertDisc;
    }

    public Date getAlertDate() {
        return this.alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate=alertDate;
    }



    public String getAlId() {
        return alId;
    }

    public void setAlId(String alId) {
        this.alId = alId;
    }

    public String getHaId() {
        return haId;
    }

    public void setHaId(String haId) {
        this.haId = haId;
    }

    public String getHabitatName() {
        return habitatName;
    }

    public void setHabitatName(String habitatName) {
        this.habitatName = habitatName;
    }
}
