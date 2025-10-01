package com.SmartHomeSystem;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Thermostat extends Device {
    private int temperature;

    public Thermostat(int id, int initialTemp) {
        super(id, "thermostat");
        this.temperature = initialTemp;
    }
     public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }

    @Override
    public String getStatus() {
        return "Thermostat " + id + ": Set to " + temperature + "°F";
    }

    @Override
    public String executeCommand(String command, String value) {
        String cmd = command.toLowerCase();
        if (cmd.equals("settemp") && value != null) {
            try {
                int newTemp = Integer.parseInt(value);
                if (newTemp >= 50 && newTemp <= 90) {
                    int oldTemp = this.temperature;
                    this.temperature = newTemp;
                    this.lastUpdate = LocalDateTime.now();
                    return "Thermostat " + id + " temperature changed from " + oldTemp + "°F to " + newTemp + "°F.";
                } else {
                    return "Error: Temperature " + newTemp + " is outside the valid range (50-90).";
                }
            } catch (NumberFormatException e) {
                return "Error: Invalid temperature value provided.";
            }
        } else {
            return "Error: Command '" + command + "' not recognized for Thermostat " + id + ".";
        }
    }

    @Override
    public void update(String command) {
        System.out.println("[Observer] Thermostat " + id + " received automated command: " + command);
        
        // Command parsing from trigger action string (e.g., "setTemp(2, 75)")
        Pattern pattern = Pattern.compile("(\\w+)\\((\\d+),\\s*(\\d+)\\)");
        Matcher matcher = pattern.matcher(command);

        if (matcher.find() && Integer.parseInt(matcher.group(2)) == this.id) {
            String cmd = matcher.group(1);
            String val = matcher.group(3);
            this.executeCommand(cmd, val);
        }
    }
}

