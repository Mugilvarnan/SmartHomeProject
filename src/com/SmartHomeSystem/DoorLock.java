package com.SmartHomeSystem;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoorLock extends Device {
    private String status; // "locked" or "unlocked"

    private void setState(String state) {
        this.status = state;
    }

    public DoorLock(int id, String initialStatus) {
        super(id, "door");
        this.status = initialStatus.toLowerCase();
        this.setState("locked");
    }
    
    public void lock() {
        // Implementation for locking the door
        System.out.println("Door is now locked.");
        setState("locked");
    }
     public void unlock() {
        // Logic to unlock the door
        System.out.println("Door unlocked.");
    }

    @Override
    public String getStatus() {
        return "Door Lock " + id + ": " + (status.equals("locked") ? "Locked" : "Unlocked");
    }

    @Override
    public String executeCommand(String command, String value) {
        String cmd = command.toLowerCase();
        if (cmd.equals("lock")) {
            this.status = "locked";
            this.lastUpdate = LocalDateTime.now();
            return "Door Lock " + id + " is now LOCKED.";
        } else if (cmd.equals("unlock")) {
            this.status = "unlocked";
            this.lastUpdate = LocalDateTime.now();
            return "Door Lock " + id + " is now UNLOCKED.";
        } else {
            return "Error: Command '" + command + "' not recognized for Door Lock " + id + ".";
        }
    }

    @Override
    public void update(String command) {
        System.out.println("[Observer] DoorLock " + id + " received automated command: " + command);
        
        // Command parsing from trigger action string (e.g., "lock(3)")
        Pattern pattern = Pattern.compile("(\\w+)\\((\\d+)\\)");
        Matcher matcher = pattern.matcher(command);

        if (matcher.find() && Integer.parseInt(matcher.group(2)) == this.id) {
            String cmd = matcher.group(1);
            this.executeCommand(cmd, null);
        }
    }
}

