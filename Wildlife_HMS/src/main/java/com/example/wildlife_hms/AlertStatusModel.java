package com.example.wildlife_hms;

public class AlertStatusModel {

    private int alertStatusId;

    private String alertStatus;

    private String remark;

    public AlertStatusModel(int alertStatusId, String alertStatus, String remark) {
        this.alertStatusId = alertStatusId;
        this.alertStatus = alertStatus;
        this.remark = remark;
    }


    public int getAlertStatusId() {
        return alertStatusId;
    }

    public void setAlertStatusId(int alertStatusId) {
        this.alertStatusId = alertStatusId;
    }

    public String getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(String alertStatus) {
        this.alertStatus = alertStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
