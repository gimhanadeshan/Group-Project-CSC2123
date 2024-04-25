package com.example.wildlife_hms;

public class SpeciesDiversityReportModel {

    private final String commonName;

    private final String scientificName;

    private final String conservationStatus;

    private final int habitatCount;

    public SpeciesDiversityReportModel(String commonName, String scientificName, String conservationStatus, int habitatCount) {
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.conservationStatus = conservationStatus;
        this.habitatCount = habitatCount;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getConservationStatus() {
        return conservationStatus;
    }

    public int getHabitatCount() {
        return habitatCount;
    }
}
