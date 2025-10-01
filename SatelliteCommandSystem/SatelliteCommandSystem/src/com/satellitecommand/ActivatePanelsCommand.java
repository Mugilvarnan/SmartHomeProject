package com.satellitecommand;

public class ActivatePanelsCommand implements Command {
    @Override
    public String execute(Satellite satellite) {
        satellite.activatePanels();
        return "Solar panels activated";
    }
}