package com.example.wildlife_hms;

import java.io.File;
import java.sql.Date;
import java.util.List;

public class ObservationsModel {

    private int observationID;

    private String obsId;
    private int habitatID;

    private int speciesID;

    private String observerName;

    private Date observationDate;

    private String notes;

    private String attachments;

    private String haId;

    private String habitatName;

    private String spId;

    private String speciesName;



    public ObservationsModel() {

    }

    public ObservationsModel(int observationID,String obsId, String observerName, Date observationDate, String notes,int habitatID,String haId,String habitatName,int speciesID,String spId,String speciesName,String attachments) {
        this.observationID = observationID;
        this.obsId = obsId;
        this.habitatID = habitatID;
        this.speciesID = speciesID;
        this.observerName = observerName;
        this.observationDate = observationDate;
        this.notes = notes;
        this.haId=haId;
        this.habitatName=habitatName;
        this.spId=spId;
        this.speciesName=speciesName;
        this.attachments = attachments;

    }

    public int getObservationID() {
        return observationID;
    }

    public void setObservationID(int observationID) {
        this.observationID = observationID;
    }

    public String getObsId() {
        return obsId;
    }

    public void setObsId(String obsId) {
        this.obsId = obsId;
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

    public String getObserverName() {
        return observerName;
    }

    public void setObserverName(String observerName) {
        this.observerName = observerName;
    }

    public Date getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(Date observationDate) {
        this.observationDate = observationDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAttachments() {

        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
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

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }
}
