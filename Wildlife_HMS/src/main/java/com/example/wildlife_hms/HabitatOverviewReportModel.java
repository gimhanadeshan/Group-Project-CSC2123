package com.example.wildlife_hms;

public class HabitatOverviewReportModel {

    private final String habitatName;

    private final double size;

    private final String description;

    private final int totalSpecies;

    private final int totalObservation;

    public HabitatOverviewReportModel(String habitatName, double size, String description, int totalSpecies, int totalObservation) {
        this.habitatName = habitatName;
        this.size = size;
        this.description = description;
        this.totalSpecies = totalSpecies;
        this.totalObservation = totalObservation;
    }

    public String getHabitatName() {
        return habitatName;
    }

    public double getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public int getTotalSpecies() {
        return totalSpecies;
    }

    public int getTotalObservation() {
        return totalObservation;
    }
}
