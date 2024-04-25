package com.example.wildlife_hms;

public class ConservationStatusModel {

    private int conservationStatusId;

    private String conservationStatus;

    private String remark;

    public ConservationStatusModel(int conservationStatusId, String conservationStatus, String remark) {
        this.conservationStatusId = conservationStatusId;
        this.conservationStatus = conservationStatus;
        this.remark = remark;
    }

    public int getConservationStatusId() {
        return conservationStatusId;
    }

    public void setConservationStatusId(int conservationStatusId) {
        this.conservationStatusId = conservationStatusId;
    }

    public String getConservationStatus() {
        return conservationStatus;
    }

    public void setConservationStatus(String conservationStatus) {
        this.conservationStatus = conservationStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
