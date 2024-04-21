package com.example.wildlife_hms;

public class EnvironmentalConditionsModel {

    private int conditionID;

    private  String conId;
    private String conditionType;

    private Double conditionValue;
    private int habitatID;

    private String haId;

    private String habitatName;

    public EnvironmentalConditionsModel(int conditionID, String conId, String conditionType, Double conditionValue, int habitatID,String haId,String habitatName) {
        this.conditionID = conditionID;
        this.conId = conId;
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
        this.habitatID = habitatID;
        this.haId=haId;
        this.habitatName=habitatName;
    }

    public EnvironmentalConditionsModel(int conditionID, String conId, String conditionType, Double conditionValue, int habitatID) {
        this.conditionID = conditionID;
        this.conId = conId;
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
        this.habitatID = habitatID;


    }

    public int getConditionID() {
        return conditionID;
    }

    public void setConditionID(int conditionID) {
        this.conditionID = conditionID;
    }

    public String getConId() {
        return conId;
    }

    public void setConId(String conId) {
        this.conId = conId;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public Double getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(Double conditionValue) {
        this.conditionValue = conditionValue;
    }

    public int getHabitatID() {
        return habitatID;
    }

    public void setHabitatID(int habitatID) {
        this.habitatID = habitatID;
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
