package com.example.wildlife_hms;

import java.sql.Date;

public class ObservationSummaryReportModel {

    private final String observerName;

    private final Date observationDate;

    private final String habitatName;

    private final String observedSpecies;

    public ObservationSummaryReportModel(String observerName, Date observationDate, String habitatName, String observedSpecies) {
        this.observerName = observerName;
        this.observationDate = observationDate;
        this.habitatName = habitatName;
        this.observedSpecies = observedSpecies;
    }

    public String getObserverName() {
        return observerName;
    }

    public Date getObservationDate() {
        return observationDate;
    }

    public String getHabitatName() {
        return habitatName;
    }

    public String getObservedSpecies() {
        return observedSpecies;
    }
}
