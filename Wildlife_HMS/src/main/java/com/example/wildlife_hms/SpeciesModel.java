package com.example.wildlife_hms;

public class SpeciesModel {

    private int id;

    private  String spId;
    private String commonName;
    private String scientificName;
    private String conservationStatus;
    private String desc;

    public SpeciesModel(int id, String spId, String commonName, String scientificName, String conservationStatus, String desc) {
        this.id = id;
        this.spId = spId;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.conservationStatus = conservationStatus;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getConservationStatus() {
        return conservationStatus;
    }

    public void setConservationStatus(String conservationStatus) {
        this.conservationStatus = conservationStatus;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
