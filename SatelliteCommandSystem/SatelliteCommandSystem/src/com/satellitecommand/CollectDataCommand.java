package com.satellitecommand;

public class CollectDataCommand implements Command {
    @Override
    public String execute(Satellite satellite) {
        int previousData = satellite.getDataCollected();
        satellite.collectData();
        int newData = satellite.getDataCollected();
        if (newData > previousData) {
            return "Data collected successfully";
        } else {
            return "Failed to collect data. Make sure solar panels are active.";
        }
    }
}