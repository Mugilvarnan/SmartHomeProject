package creational.prototypepattern;

public class CarFeatures {
    private String color;
    private String audioSystem;

    public CarFeatures(String color, String audioSystem) {
        this.color = color;
        this.audioSystem = audioSystem;
    }

    public String getColor() { return color; }
    public String getAudioSystem() { return audioSystem; }
    public void setColor(String color) { this.color = color; }

    @Override
    public String toString() {
        return "{Color=" + color + ", Audio=" + audioSystem + "}";
    }
}

