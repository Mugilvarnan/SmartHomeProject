package com.SmartHomeSystem;

import java.time.LocalDateTime;
import java.time.LocalTime; // Keeping LocalTime as it's used inside checkSchedules()
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Custom class imports (assuming they are defined within the com.SmartHomeSystem package)
// Note: Removed unused regex imports (Matcher, Pattern) as they belong in DeviceControlProxy.

/**
 * The core subject that manages all devices and automation rules.
 * It coordinates communication and executes scheduled/triggered commands.
 */
public class SmartHomeHub {
    private final Map<Integer, Device> devices = new ConcurrentHashMap<>();
    private final List<Map<String, Object>> schedules = new ArrayList<>();
    private final List<Map<String, Object>> triggers = new ArrayList<>();
    // DeviceFactory, Device, Thermostat, DoorLock, DeviceControlProxy are assumed
    // to be available in this package.
    private final DeviceFactory factory = new DeviceFactory();
    
    // Reference to the Proxy to delegate command execution (Dependency Inversion Principle)
    private DeviceControlProxy proxy; 

    private LocalDateTime currentTime;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public SmartHomeHub(LocalDateTime initialTime) {
        this.currentTime = initialTime;
    }

    /**
     * Setter to inject the Proxy reference from the Simulator.
     */
    public void setProxy(DeviceControlProxy proxy) {
        this.proxy = proxy;
    }

    public void setCurrentTime(LocalDateTime time) {
        this.currentTime = time;
    }
    
    // --- Device Management ---

    /**
     * Creates a device using the Factory and registers it in the Hub.
     */
    public String addDevice(Map<String, Object> data) {
        int id = (int) data.get("id");
        String type = (String) data.get("type");
        
        if (devices.containsKey(id)) {
            return "[System] Error: Device ID " + id + " already exists.";
        }

        Device newDevice = factory.createDevice(data);
        if (newDevice != null) {
            devices.put(id, newDevice);
            return "[System] Added new device: " + newDevice.getDeviceType() + " " + id + ".";
        }
        return "[System] Error: Unknown device type: " + type;
    }

    /**
     * Removes a device from the system and cleans up related automations.
     */
    public String removeDevice(int id) {
        if (!devices.containsKey(id)) {
            return "[System] Error: Device " + id + " not found to remove.";
        }
        
        Device removedDevice = devices.remove(id);
        
        // Clean up schedules and triggers related to the removed device
        schedules.removeIf(s -> (int)s.get("device_id") == id);
        triggers.removeIf(t -> {
            String action = (String) t.get("action");
            // Check if action string contains the removed device's ID
            return action != null && action.contains("(" + id + ")");
        });

        return "[System] Successfully removed " + removedDevice.getDeviceType() + " " + id + 
               ". Associated schedules/triggers cleared.";
    }

    public Device getDevice(int id) {
        return devices.get(id);
    }
    
    // --- Command Processing (Delegated by Proxy) ---

    /**
     * Processes a standard device command (e.g., turnOn, setTemp, lock).
     */
    public String processCommand(int id, String command, String value) {
        Device device = devices.get(id);
        if (device == null) return "[Hub] Error: Device " + id + " not found.";

        try {
            switch (command.toLowerCase()) {
                case "turnon":
                    device.turnOn();
                    return device.getDeviceType() + " " + id + " turned ON.";
                case "turnoff":
                    device.turnOff();
                    return device.getDeviceType() + " " + id + " turned OFF.";
                case "settemp":
                    if (value != null) {
                        int temp = Integer.parseInt(value);
                        if (device instanceof Thermostat) {
                            ((Thermostat) device).setTemperature(temp);
                            return device.getDeviceType() + " " + id + " set to " + temp + "°F.";
                        }
                    }
                    return "Error: Invalid setTemp command or device type.";
                case "lock":
                    if (device instanceof DoorLock) {
                        ((DoorLock) device).lock();
                        return device.getDeviceType() + " " + id + " locked.";
                    }
                    return "Error: Device not a door lock.";
                case "unlock":
                    if (device instanceof DoorLock) {
                        ((DoorLock) device).unlock();
                        return device.getDeviceType() + " " + id + " unlocked.";
                    }
                    return "Error: Device not a door lock.";
                default:
                    return "Error: Unknown command: " + command;
            }
        } catch (NumberFormatException e) {
            return "Error: Invalid value provided for command " + command;
        } catch (Exception e) {
            return "Error executing command: " + e.getMessage();
        }
    }
    
    // --- Automation Management ---

    public String addSchedule(int deviceId, String time, String command) {
        Map<String, Object> schedule = new HashMap<>();
        schedule.put("device_id", deviceId);
        schedule.put("time", time);
        schedule.put("command", command); 
        schedules.add(schedule);
        return "[System] Schedule added: Device " + deviceId + " at " + time + " for '" + command + "'.";
    }

    public String addTrigger(String type, String op, double val, String action) {
        Map<String, Object> trigger = new HashMap<>();
        trigger.put("type", type.toLowerCase());
        trigger.put("op", op);
        trigger.put("val", val);
        trigger.put("action", action);
        trigger.put("last_fired", null); // Track last fired time
        triggers.add(trigger);
        return "[System] Trigger added: If " + type + " " + op + " " + val + " then '" + action + "'.";
    }

    public List<Map<String, Object>> getSchedules() {
        return schedules;
    }

    public List<Map<String, Object>> getTriggers() {
        return triggers;
    }
    
    // --- Automation Execution ---

    /**
     * Checks all schedules against the current simulation time.
     * Executes the action using the injected Proxy.
     */
    public List<String> checkSchedules(LocalDateTime current) {
        List<String> results = new ArrayList<>();
        LocalTime nowTime = current.toLocalTime();
        String nowStr = nowTime.format(TIME_FORMATTER);

        for (Map<String, Object> schedule : schedules) {
            String scheduleTimeStr = (String) schedule.get("time");
            
            if (scheduleTimeStr.equals(nowStr)) {
                String command = (String) schedule.get("command");
                
                // CRITICAL FIX: Use the Proxy to execute the action command.
                // This re-uses the reliable parsing logic from the Proxy, fixing the ID mismatch error.
                String actionResult = proxy.executeUserCommand(command);

                results.add("[Schedule Fired @" + nowStr + "] " + actionResult.replaceAll("\n", " | "));
            }
        }
        return results;
    }

    /**
     * Checks all triggers against the current device states.
     * Triggers are executed via the Proxy.
     */
    public List<String> checkTriggers() {
        List<String> results = new ArrayList<>();
        LocalDateTime now = this.currentTime;
        
        for (Map<String, Object> trigger : triggers) {
            String type = (String) trigger.get("type");
            String op = (String) trigger.get("op");
            double val = (double) trigger.get("val");
            String action = (String) trigger.get("action");
            
            // NEW: Check if the trigger has fired in the current minute (or recently)
            LocalDateTime lastFired = (LocalDateTime) trigger.get("last_fired");
            if (lastFired != null && lastFired.getMinute() == now.getMinute() && lastFired.getHour() == now.getHour()) {
                continue; // Skip if already fired in the current minute
            }

            boolean conditionMet = false;
            
            // Currently only supports 'temperature' condition
            if (type.equalsIgnoreCase("temperature")) {
                // Find highest temperature among all thermostats
                double currentTemp = devices.values().stream()
                    .filter(d -> d instanceof Thermostat)
                    .mapToDouble(d -> ((Thermostat) d).getTemperature())
                    .max()
                    .orElse(Double.MIN_VALUE);
                
                switch (op) {
                    case ">": conditionMet = currentTemp > val; break;
                    case "<": conditionMet = currentTemp < val; break;
                    case "=": conditionMet = currentTemp == val; break;
                }
                
                if (conditionMet) {
                    // Execute action using the Proxy
                    String actionResult = proxy.executeUserCommand(action);
                    
                    results.add("[Trigger Fired] Condition '" + type + " " + op + " " + val + 
                                "' met. Action: '" + action + "'.");
                    // NEW: Update last_fired time to prevent immediate re-firing
                    trigger.put("last_fired", now);
                    results.add(actionResult.replaceAll("\n", " | "));
                } else if (lastFired != null) {
                    // Reset last_fired if the condition is no longer met, allowing it to fire again later
                    trigger.put("last_fired", null);
                }
            }
        }
        return results;
    }

    // --- Status Reporting ---
    
    public String getAllDeviceStatuses() {
        StringBuilder sb = new StringBuilder("System Status:\n");
        devices.values().stream()
            .sorted((d1, d2) -> Integer.compare(d1.getDeviceId(), d2.getDeviceId()))
            .forEach(d -> sb.append("  - ").append(d.getDeviceType()).append(" ").append(d.getDeviceId()).append(": ").append(d.getState()).append("\n"));
        return sb.toString();
    }
}
