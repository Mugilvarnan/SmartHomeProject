package com.satellitecommand;

public class Satellite {
    private String orientation;
    private boolean solarPanelsActive;
    private int dataCollected;

    public Satellite() {
        this.orientation = "North";
        this.solarPanelsActive = false;
        this.dataCollected = 0;
    }

    public void rotate(String direction) {
        this.orientation = direction;
    }

    public void activatePanels() {
        this.solarPanelsActive = true;
    }

    public void deactivatePanels() {
        this.solarPanelsActive = false;
    }

    public void collectData() {
        if (solarPanelsActive) {
            this.dataCollected += 10;
        }
    }

    public String getOrientation() {
        return orientation;
    }

    public boolean areSolarPanelsActive() {
        return solarPanelsActive;
    }

    public int getDataCollected() {
        return dataCollected;
    }

    @Override
    public String toString() {
        return String.format("Orientation: %s, Solar Panels: %s, Data Collected: %d",
                orientation,
                solarPanelsActive ? "Active" : "Inactive",
                dataCollected);
    }
}