package com.SmartHomeSystem;
import java.time.LocalDateTime;

/**
 * Base Device Interface (Polymorphism and Inheritance).
 * All smart devices must implement these core functionalities.
 */
public abstract class Device implements Observer {
    protected int id;
    protected String type;
    protected LocalDateTime lastUpdate;

    public Device(int id, String type) {
        this.id = id;
        this.type = type;
        this.lastUpdate = LocalDateTime.now();
        System.out.println("[Factory] Created " + type + " " + id);
    }
    public String getDeviceType() {
        return this.getClass().getSimpleName(); // Returns the class name as the device type
    }
    public int getId() {
        return id;
    }

    // Convenience accessor used across the hub (keeps naming consistent with callers)
    public int getDeviceId() {
        return this.id;
    }

    // Return a simple state string for status reporting. Subclasses may override to provide richer state.
    public String getState() {
        // Default uses getStatus() but strips the leading device label if present
        String status = getStatus();
        return status != null ? status : "Unknown";
    }

    /**
     * Basic on/off commands. Default implementations delegate to executeCommand so
     * concrete devices that support these commands don't need to implement additional methods.
     */
    public void turnOn() {
        // Delegate to executeCommand; ignore returned message
        try {
            executeCommand("turnOn", null);
        } catch (AbstractMethodError e) {
            // If subclass didn't implement executeCommand, no-op
        }
    }

    public void turnOff() {
        try {
            executeCommand("turnOff", null);
        } catch (AbstractMethodError e) {
            // no-op
        }
    }

    public String getType() {
        return type;
    }

    /**
     * Returns the current status of the device in a formatted string.
     */
    public abstract String getStatus();

    /**
     * Executes a specific command on the device.
     * @param command The action command (e.g., "turnOn", "setTemp").
     * @param value Optional parameter for the command (e.g., temperature).
     * @return A status message regarding the execution result.
     */
    public abstract String executeCommand(String command, String value);
}
