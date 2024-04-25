package com.example.wildlife_hms;

public class AlertTypeModel {

    private int alertTypeId;

    private String alertType;


    private String remark;

    public AlertTypeModel(int alertTypeId, String alertType, String remark) {
        this.alertTypeId = alertTypeId;
        this.alertType = alertType;
        this.remark = remark;
    }


    public int getAlertTypeId() {
        return alertTypeId;
    }

    public void setAlertTypeId(int alertTypeId) {
        this.alertTypeId = alertTypeId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
