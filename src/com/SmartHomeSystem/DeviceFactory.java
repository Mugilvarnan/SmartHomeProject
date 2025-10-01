package com.SmartHomeSystem;
import java.util.Map;

/**
 * Factory Method (Creational Pattern) to create different types of devices.
 */
public class DeviceFactory {

    /**
     * Creates a new Device instance based on the provided data map.
     * @param deviceData Map containing "id", "type", and initial state information.
     * @return A concrete Device object.
     * @throws IllegalArgumentException if the device type is unknown.
     */
    public static Device createDevice(Map<String, Object> deviceData) throws IllegalArgumentException {
        int deviceId = (int) deviceData.get("id");
        String deviceType = ((String) deviceData.get("type")).toLowerCase();

        switch (deviceType) {
            case "light":
                String lightStatus = (String) deviceData.getOrDefault("status", "off");
                return new Light(deviceId, lightStatus);
            case "thermostat":
                // Handles Integer for initial input
                Object tempObj = deviceData.getOrDefault("temperature", 70);
                int initialTemp = tempObj instanceof Integer ? (int) tempObj : Integer.parseInt(tempObj.toString());
                return new Thermostat(deviceId, initialTemp);
            case "door":
                String doorStatus = (String) deviceData.getOrDefault("status", "locked");
                return new DoorLock(deviceId, doorStatus);
            default:
                throw new IllegalArgumentException("Unknown device type: " + deviceType);
        }
    }
}

