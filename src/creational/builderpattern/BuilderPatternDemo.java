package creational.builderpattern;

public class BuilderPatternDemo {
    public static void main(String[] args) {
        // ---- Build Sports Car ----
        CarBuilder sportsCarBuilder = new SportsCarBuilder();
        Director director = new Director(sportsCarBuilder);
        director.constructCar();
        Car sportsCar = sportsCarBuilder.getCar();
        System.out.println("Sports Car: " + sportsCar);

        // ---- Build Family Car ----
        CarBuilder familyCarBuilder = new FamilyCarBuilder();
        director = new Director(familyCarBuilder);
        director.constructCar();
        Car familyCar = familyCarBuilder.getCar();
        System.out.println("Family Car: " + familyCar);
    }
}

