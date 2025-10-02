package creational.prototypepattern;

public class PrototypePatternDemo {
    public static void main(String[] args) {
        // Original SportsCar
        CarFeatures features = new CarFeatures("Red", "Bose");
        Car sportsCar = new Car("V8 Engine", 2, true, true, features);

        // Add to registry
        PrototypeRegistry registry = new PrototypeRegistry();
        registry.addPrototype("SportsCar", sportsCar);

        // Shallow clone via registry
        Car shallowClone = (Car) registry.getPrototype("SportsCar");
        System.out.println("Shallow Clone: " + shallowClone);

        // Deep clone directly
        Car deepClone = (Car) sportsCar.deepClone();
        System.out.println("Deep Clone: " + deepClone);

        // Modify colors to see the difference
        System.out.println("\n--- Changing Deep Clone color to Blue ---");
        deepClone.getFeatures().setColor("Blue");

        System.out.println("Original SportsCar: " + sportsCar);
        System.out.println("Shallow Clone: " + shallowClone);
        System.out.println("Deep Clone (after color change): " + deepClone);
    }
}

