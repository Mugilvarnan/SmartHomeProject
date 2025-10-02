package behavioural.strategy;

public class BitcoinPayment implements PaymentStrategy {
    @Override
    public void pay(float amount) {
        System.out.println("Paying " + amount + " using Bitcoin");
    }
}
