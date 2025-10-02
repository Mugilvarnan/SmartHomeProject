package structural.bridgepattern;

public class BridgePatternDemo {
    public static void main(String[] args) {
        // Send via Email
        Notifier emailNotifier = new AlertNotifier(new EmailMessageSender());
        emailNotifier.notifyUser("Your order #1234 has been shipped!");

        // Send via SMS
        Notifier smsNotifier = new AlertNotifier(new SMSMessageSender());
        smsNotifier.notifyUser("Your order #1234 has been delivered!");
    }
}

