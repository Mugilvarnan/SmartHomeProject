package creational.builderpattern;

public class FamilyCarBuilder implements CarBuilder {
    private Car car = new Car();

    public void buildEngine() { car.setEngine("V4 Engine"); }
    public void buildSeats() { car.setSeats(5); }
    public void buildGPS() { car.setGPS(true); }
    public void buildSunroof() { car.setSunroof(false); }

    public Car getCar() { return car; }
}

