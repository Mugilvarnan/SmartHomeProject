package com.satellitecommand;

import java.util.logging.Logger;
import java.util.logging.Level;

public class SatelliteCommandSystem {
    private final Satellite satellite;
    private static final Logger logger = Logger.getLogger(SatelliteCommandSystem.class.getName());

    public SatelliteCommandSystem() {
        this.satellite = new Satellite();
    }

    public String executeCommand(String input) {
        String[] parts = input.split("\\s+");
        String commandType = parts[0].toLowerCase();

        try {
            Command command;
            switch (commandType) {
                case "rotate":
                    if (parts.length < 2) {
                        throw new IllegalArgumentException("Rotate command requires a direction");
                    }
                    command = new RotateCommand(parts[1]);
                    break;
                case "activatepanels":
                    command = new ActivatePanelsCommand();
                    break;
                case "deactivatepanels":
                    command = new DeactivatePanelsCommand();
                    break;
                case "collectdata":
                    command = new CollectDataCommand();
                    break;
                case "status":
                    return satellite.toString();
                default:
                    throw new IllegalArgumentException("Unknown command: " + commandType);
            }

            String result = command.execute(satellite);
            logger.info("Command executed: " + input + " - Result: " + result);
            return result;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error executing command: " + input, e);
            throw new IllegalArgumentException("Error executing command: " + e.getMessage());
        }
    }
}
