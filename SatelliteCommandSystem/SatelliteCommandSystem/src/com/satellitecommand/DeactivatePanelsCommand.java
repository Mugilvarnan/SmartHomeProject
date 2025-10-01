package com.satellitecommand;

public class DeactivatePanelsCommand implements Command {
    @Override
    public String execute(Satellite satellite) {
        satellite.deactivatePanels();
        return "Solar panels deactivated";
    }
}
