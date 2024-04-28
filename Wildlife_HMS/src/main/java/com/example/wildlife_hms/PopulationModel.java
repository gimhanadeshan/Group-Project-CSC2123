package com.example.wildlife_hms;

import java.sql.Date;

public class PopulationModel {
    private int populationID;

    private String poId;
   private int habitatID;
   private int speciesID;
   private int populationSize;
   private Date lastSurveyDate;

   private  String habitatName;

   private String speciesName;

   private  String haId;

   private String spId;




    public PopulationModel(int populationID,String poId, int habitatID,String haId,String habitatName, int speciesID,String spId,String speciesName, int populationSize, Date lastSurveyDate) {
        this.populationID = populationID;
        this.poId = poId;
        this.habitatID = habitatID;
        this.speciesID = speciesID;
        this.populationSize = populationSize;
        this.lastSurveyDate = lastSurveyDate;
        this.haId=haId;
        this.habitatName=habitatName;
        this.spId=spId;
        this.speciesName=speciesName;
    }

    public int getPopulationID() {
        return populationID;
    }

    public void setPopulationID(int populationID) {
        this.populationID = populationID;
    }

    public int getHabitatID() {
        return habitatID;
    }

    public void setHabitatID(int habitatID) {
        this.habitatID = habitatID;
    }

    public int getSpeciesID() {
        return speciesID;
    }

    public void setSpeciesID(int speciesID) {
        this.speciesID = speciesID;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public Date getLastSurveyDate() {
        return lastSurveyDate;
    }

    public void setLastSurveyDate(Date lastSurveyDate) {
        this.lastSurveyDate = lastSurveyDate;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }


    public String getHabitatName() {
        return habitatName;
    }

    public void setHabitatName(String habitatName) {
        this.habitatName = habitatName;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getHaId() {
        return haId;
    }

    public void setHaId(String haId) {
        this.haId = haId;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }
}
