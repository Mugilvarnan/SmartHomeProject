package com.SmartHomeSystem;
/**
 * Observer Interface (Behavioral Pattern).
 * All concrete devices must implement this to receive automated commands from the Hub.
 */
public interface Observer {
    /**
     * Receives an update/command from the Subject (SmartHomeHub).
     * @param command The full command string (e.g., "turnOff(1)" or "setTemp(2, 75)").
     */
    void update(String command);
}

