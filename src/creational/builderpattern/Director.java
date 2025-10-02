package creational.builderpattern;

public class Director {
    private CarBuilder builder;

    public Director(CarBuilder builder) {
        this.builder = builder;
    }

    public void constructCar() {
        builder.buildEngine();
        builder.buildSeats();
        builder.buildGPS();
        builder.buildSunroof();
    }
}

