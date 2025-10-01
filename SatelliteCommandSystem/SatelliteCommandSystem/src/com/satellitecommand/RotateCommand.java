package com.satellitecommand;

public class RotateCommand implements Command {
    private final String direction;

    public RotateCommand(String direction) {
        this.direction = direction;
    }

    @Override
    public String execute(Satellite satellite) {
        satellite.rotate(direction);
        return "Satellite rotated to " + direction;
    }
}
