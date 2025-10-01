package com.SmartHomeSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Proxy (Structural Pattern) to control access and add operational logging/validation
 * to the SmartHomeHub.
 */
public class DeviceControlProxy {
    private SmartHomeHub hub;

    public DeviceControlProxy(SmartHomeHub hub) {
        this.hub = hub;
    }

    /**
     * Public method for users to send commands.
     * Performs validation and logging before delegation.
     */
    public String executeUserCommand(String commandStr) {
        System.out.println("[Proxy] Receiving command: '" + commandStr + "'");

        // CRITICAL FIX: Updated regex to use a non-greedy quantifier (.*?) for the VALUE group (3).
        // This ensures the regex captures everything, including nested parentheses and quotes,
        // up to the final closing parenthesis of the main command, preventing premature argument termination.
        Pattern pattern = Pattern.compile("^(\\w+)\\((\\d+)(?:,\\s*(.*?))?\\)$");
        Matcher matcher = pattern.matcher(commandStr.trim());

        if (!matcher.find()) {
            return "Error: Invalid command format. Use 'COMMAND(ID)' or 'COMMAND(ID, VALUE)' (e.g., turnOn(1) or setTemp(2, 75)).";
        }

        String command = matcher.group(1);
        int deviceId;
        try {
            deviceId = Integer.parseInt(matcher.group(2));
        } catch (NumberFormatException e) {
            return "Error: Device ID must be an integer.";
        }
        // Group 3 is the value, which may contain commas and quotes 
        String value = matcher.group(3); 

        // 1. Access Control / Existence Check (skip for configuration commands)
        // Configuration commands must be skipped here because the device ID might refer to a non-existent device (for addDevice)
        // or a conceptual ID (0 for addTrigger).
        if (hub.getDevice(deviceId) == null && 
            !command.equalsIgnoreCase("addDevice") &&
            !command.equalsIgnoreCase("setSchedule") &&
            !command.equalsIgnoreCase("addTrigger") &&
            !command.equalsIgnoreCase("removedevice"))
        {
            return "[Proxy] Error: Device " + deviceId + " not found in the system.";
        }

        // 2. Delegation to Hub for execution
        String lowerCommand = command.toLowerCase();

        if (lowerCommand.equals("removedevice")) {
            // No value is needed for removeDevice, just the ID
            return hub.removeDevice(deviceId);

        } else if (lowerCommand.equals("adddevice")) {
            // Expected value format: TYPE,STATUS/TEMP (e.g., light,on or thermostat,70)
            if (value != null) {
                String[] parts = value.split(",", 2); // Split only on the first comma
                if (parts.length >= 2) {
                    Map<String, Object> deviceData = new HashMap<>();
                    deviceData.put("id", deviceId);
                    deviceData.put("type", parts[0].trim());
                    String param = parts[1].trim();
                    
                    if (parts[0].trim().equalsIgnoreCase("thermostat")) {
                        try {
                            deviceData.put("temperature", Integer.parseInt(param));
                        } catch (NumberFormatException e) {
                            return "Error: Invalid temperature for new thermostat.";
                        }
                    } else {
                        deviceData.put("status", param);
                    }
                    return hub.addDevice(deviceData);
                }
            }
            return "Error: Invalid arguments for addDevice. Use 'addDevice(ID, TYPE, STATUS/TEMP)'.";

        } else if (lowerCommand.equals("setschedule")) {
            // Expected value format: "HH:MM","COMMAND_ACTION"
            if (value != null) {
                String[] parts = value.split(",", 2); 
                if (parts.length == 2) {
                    // FIX: Remove surrounding quotes from TIME and ACTION strings
                    String timeStr = parts[0].trim().replaceAll("^\"|\"$", "").replaceAll("^'|'$", "");
                    String actionStr = parts[1].trim().replaceAll("^\"|\"$", "").replaceAll("^'|'$", "");
                    
                    if (timeStr.isEmpty() || actionStr.isEmpty()) {
                         return "Error: Schedule time and action must be specified and cannot be empty after parsing.";
                    }
                    
                    return hub.addSchedule(deviceId, timeStr, actionStr);
                }
            }
            return "Error: Invalid arguments for setSchedule. Use 'setSchedule(ID, \"HH:MM\", \"ACTION\")'.";
        
        } else if (lowerCommand.equals("addtrigger")) {
            // Expected value format: TYPE,OP,VAL,ACTION 
            if (value != null) {
                String[] parts = value.split(",", 4);
                if (parts.length == 4) {
                    // FIX: Remove surrounding quotes from TYPE and ACTION strings
                    String conditionType = parts[0].trim().replaceAll("^\"|\"$", "").replaceAll("^'|'$", "");
                    String operator = parts[1].trim().replaceAll("^\"|\"$", "").replaceAll("^'|'$", "");
                    double val;
                    try {
                        val = Double.parseDouble(parts[2].trim());
                    } catch (NumberFormatException e) {
                        return "Error: Invalid numeric value for trigger condition.";
                    }
                    String actionStr = parts[3].trim().replaceAll("^\"|\"$", "").replaceAll("^'|'$", "");
                    
                    // The device ID is ignored (0 is used) in the Proxy for addTrigger but required by the regex format
                    return hub.addTrigger(conditionType, operator, val, actionStr);
                }
            }
            return "Error: Invalid arguments for addTrigger. Use 'addTrigger(ID_IGNORED, TYPE, OP, VAL, ACTION)'.";
        }

        // Standard device command delegation
        return hub.processCommand(deviceId, command, value);
    }
}
