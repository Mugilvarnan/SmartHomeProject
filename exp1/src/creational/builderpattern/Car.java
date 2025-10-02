package creational.builderpattern;

public class Car {
    private String engine;     // type of engine
    private int seats;         // number of seats
    private boolean gps;       // has GPS?
    private boolean sunroof;   // has Sunroof?

    public void setEngine(String engine) { this.engine = engine; }
    public void setSeats(int seats) { this.seats = seats; }
    public void setGPS(boolean gps) { this.gps = gps; }
    public void setSunroof(boolean sunroof) { this.sunroof = sunroof; }

    @Override
    public String toString() {
        return "Car [Engine=" + engine + 
               ", Seats=" + seats + 
               ", GPS=" + gps + 
               ", Sunroof=" + sunroof + "]";
    }
}

