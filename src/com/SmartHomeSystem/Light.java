package com.SmartHomeSystem;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Light extends Device {
    private String status; // "on" or "off"

    public Light(int id, String initialStatus) {
        super(id, "light");
        this.status = initialStatus.toLowerCase();
    }

    @Override
    public String getStatus() {
        return "Light " + id + ": " + (status.equals("on") ? "On" : "Off");
    }

    @Override
    public String executeCommand(String command, String value) {
        String cmd = command.toLowerCase();
        if (cmd.equals("turnon")) {
            this.status = "on";
            this.lastUpdate = LocalDateTime.now();
            return "Light " + id + " is now ON.";
        } else if (cmd.equals("turnoff")) {
            this.status = "off";
            this.lastUpdate = LocalDateTime.now();
            return "Light " + id + " is now OFF.";
        } else {
            return "Error: Command '" + command + "' not recognized for Light " + id + ".";
        }
    }

    @Override
    public void update(String command) {
        System.out.println("[Observer] Light " + id + " received automated command: " + command);
        
        // Command parsing from trigger action string (e.g., "turnOff(1)")
        Pattern pattern = Pattern.compile("(\\w+)\\((\\d+)\\)");
        Matcher matcher = pattern.matcher(command);

        if (matcher.find() && Integer.parseInt(matcher.group(2)) == this.id) {
            String cmd = matcher.group(1);
            this.executeCommand(cmd, null);
        }
    }
}

