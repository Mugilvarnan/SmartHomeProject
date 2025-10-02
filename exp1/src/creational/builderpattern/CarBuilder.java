package creational.builderpattern;

public interface CarBuilder {
    void buildEngine();
    void buildSeats();
    void buildGPS();
    void buildSunroof();
    Car getCar();
}

