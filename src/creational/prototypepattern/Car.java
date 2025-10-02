package creational.prototypepattern;

public class Car implements Prototype {
    private String engine;
    private int seats;
    private boolean gps;
    private boolean sunroof;
    private CarFeatures features; // nested object

    public Car(String engine, int seats, boolean gps, boolean sunroof, CarFeatures features) {
        this.engine = engine;
        this.seats = seats;
        this.gps = gps;
        this.sunroof = sunroof;
        this.features = features;
    }

    // Shallow Copy: shares the same CarFeatures reference
    @Override
    public Prototype clone() {
        return new Car(engine, seats, gps, sunroof, features);
    }

    // Deep Copy: creates a fresh CarFeatures object too
    public Prototype deepClone() {
        CarFeatures clonedFeatures = new CarFeatures(features.getColor(), features.getAudioSystem());
        return new Car(engine, seats, gps, sunroof, clonedFeatures);
    }

    public CarFeatures getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return "Car [Engine=" + engine +
               ", Seats=" + seats +
               ", GPS=" + gps +
               ", Sunroof=" + sunroof +
               ", Features=" + features + "]";
    }
}

