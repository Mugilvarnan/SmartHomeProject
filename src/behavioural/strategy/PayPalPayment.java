package behavioural.strategy;

public class PayPalPayment implements PaymentStrategy {
    @Override
    public void pay(float amount) {
        System.out.println("Paying " + amount + " using PayPal");
    }
}
