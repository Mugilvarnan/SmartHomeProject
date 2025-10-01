package com.SmartHomeSystem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SmartHomeSimulator {
    
    // Helper method to create the initial device configuration map
    private static List<Map<String, Object>> parseInitialDevices() {
        List<Map<String, Object>> initialData = new ArrayList<>();
        
        Map<String, Object> lightData = new HashMap<>();
        lightData.put("id", 1);
        lightData.put("type", "light");
        lightData.put("status", "off");
        initialData.add(lightData);
        
        Map<String, Object> thermostatData = new HashMap<>();
        thermostatData.put("id", 2);
        thermostatData.put("type", "thermostat");
        thermostatData.put("temperature", 70);
        initialData.add(thermostatData);
        
        Map<String, Object> doorData = new HashMap<>();
        doorData.put("id", 3);
        doorData.put("type", "door");
        doorData.put("status", "locked");
        initialData.add(doorData);
        
        return initialData;
    }

    private static void displayAutomationStatus(SmartHomeHub hub) {
        System.out.println("\nScheduled Tasks:");
        if (hub.getSchedules().isEmpty()) {
            System.out.println("  - No schedules set.");
        } else {
            for (Map<String, Object> schedule : hub.getSchedules()) {
                System.out.printf("  - Device %d at %s: %s%n", schedule.get("device_id"), schedule.get("time"), schedule.get("command"));
            }
        }
        
        System.out.println("\nAutomated Triggers:");
        if (hub.getTriggers().isEmpty()) {
            System.out.println("  - No triggers set.");
        } else {
            for (Map<String, Object> trigger : hub.getTriggers()) {
                LocalDateTime lastFired = (LocalDateTime) trigger.get("last_fired");
                String lastFiredStr = lastFired != null ? 
                    lastFired.format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "Never";
                System.out.printf("  - If %s %s %.0f then %s (Last Fired: %s)%n", 
                    trigger.get("type"), trigger.get("op"), (double)trigger.get("val"), trigger.get("action"), lastFiredStr);
            }
        }
        System.out.println("----------------------------------------");
    }

    /**
     * Handles dynamic setup of initial schedules and triggers by user input.
     */
    private static void runInitialAutomationSetup(Scanner scanner, DeviceControlProxy proxy) {
        System.out.println("\n--- Initial Automation Setup ---");
        System.out.println("Enter your schedules and triggers now. Type 'done' when finished.");
        System.out.println("Available commands for setup:");
        System.out.println(" - Schedule: setSchedule(ID, \"HH:MM\", \"COMMAND(ID, VALUE)\")");
        System.out.println(" - Trigger: addTrigger(IGN, TYPE, OP, VAL, ACTION)");
        System.out.println("   (Example: setSchedule(2, \"06:00\", \"setTemp(2, 75)\") or addTrigger(0, temperature, >, 75, turnOff(1)))");
        
        while (true) {
            System.out.print("[Setup] > ");
            String setupInput = scanner.nextLine().trim();
            
            if (setupInput.equalsIgnoreCase("done")) {
                break;
            }

            // Only allow schedule and trigger commands during setup
            String lowerInput = setupInput.toLowerCase();
            if (lowerInput.startsWith("setschedule") || lowerInput.startsWith("addtrigger")) {
                String result = proxy.executeUserCommand(setupInput);
                System.out.println("[Setup Result] " + result);
            } else {
                System.out.println("Error: Only 'setSchedule', 'addTrigger', or 'done' allowed during initial setup.");
            }
        }
    }

    public static void main(String[] args) {
        // Simulation Start Time
        LocalDateTime simTime = LocalDateTime.of(2024, 1, 1, 5, 58);
        DateTimeFormatter timeDisplayFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Initialize Hub and Proxy
        SmartHomeHub hub = new SmartHomeHub(simTime);
        Scanner scanner = new Scanner(System.in);
        DeviceControlProxy proxy = new DeviceControlProxy(hub);
        
        // CRITICAL FIX: Inject the proxy into the hub so the hub can execute schedules reliably
        hub.setProxy(proxy); 

        System.out.println("--- Smart Home System Initializing ---");
        
        // Use Factory via Hub to create and add initial devices
        for (Map<String, Object> deviceData : parseInitialDevices()) {
            hub.addDevice(deviceData);
        }

        // Run the new interactive setup for automation
        runInitialAutomationSetup(scanner, proxy);

        System.out.println("\n--- Starting Simulation ---");
        System.out.println("Time: " + simTime.format(timeDisplayFormatter));
        System.out.println(hub.getAllDeviceStatuses());
        displayAutomationStatus(hub);
        System.out.println("----------------------------------------");

        // Simulation Loop - Now event-driven, time only advances on 'next' command.
        while (true) {
            hub.setCurrentTime(simTime); 

            // 1. Check Triggers (Run immediately to reflect state changes from previous manual commands)
            List<String> triggerResults = hub.checkTriggers();
            for (String msg : triggerResults) {
                System.out.println(msg);
            }
            
            // Display current status before prompting for a command
            System.out.println(hub.getAllDeviceStatuses());

            // 2. User Input (Manual Control)
            System.out.println("Time: " + simTime.format(timeDisplayFormatter));
            // Updated command prompt to include all new functionality
            System.out.println("Enter Command (e.g., turnOn(1), setTemp(2, 68), addDevice(5, light, on), removeDevice(1), next, status, exit):");
            System.out.print("> ");
            
            String userInput = scanner.nextLine().trim();

            if (userInput.equalsIgnoreCase("exit")) {
                System.out.println("Exiting Smart Home Simulation.");
                break;
            } else if (userInput.equalsIgnoreCase("status")) {
                System.out.println("\n--- System Status Report ---");
                System.out.println("Time: " + simTime.format(timeDisplayFormatter));
                displayAutomationStatus(hub);
            } else if (userInput.equalsIgnoreCase("next")) {
                // Time Advance: Move to the next minute
                simTime = simTime.plusMinutes(1);
                hub.setCurrentTime(simTime); 
                System.out.println("\n[TIME ADVANCE] Current Time: " + simTime.format(timeDisplayFormatter));

                // Check time-based automation
                List<String> scheduleResults = hub.checkSchedules(simTime);
                for (String msg : scheduleResults) {
                    System.out.println(msg);
                }

                // If time advances, we immediately loop back to check triggers against new schedules/time
                continue; 
            } else {
                // Any device command goes through the Proxy (Time remains static for this event)
                String result = proxy.executeUserCommand(userInput);
                System.out.println("[Result] " + result);
            }
        }
        scanner.close();
    }
}
