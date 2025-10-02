package behavioural.strategy;

public class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(float amount) {
        System.out.println("Paying " + amount + " using Credit Card");
    }
}
