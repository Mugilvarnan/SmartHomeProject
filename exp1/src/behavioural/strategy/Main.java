package behavioural.strategy;

public class Main {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor(new CreditCardPayment());
        processor.processPayment(100);

        processor.setStrategy(new PayPalPayment());
        processor.processPayment(200);

        processor.setStrategy(new BitcoinPayment());
        processor.processPayment(300);
    }
}