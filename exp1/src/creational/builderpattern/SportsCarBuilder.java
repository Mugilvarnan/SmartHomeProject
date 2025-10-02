package creational.builderpattern;

public class SportsCarBuilder implements CarBuilder {
    private Car car = new Car();

    public void buildEngine() { car.setEngine("V8 Engine"); }
    public void buildSeats() { car.setSeats(2); }
    public void buildGPS() { car.setGPS(true); }
    public void buildSunroof() { car.setSunroof(true); }

    public Car getCar() { return car; }
}

