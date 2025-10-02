package structural.bridgepattern;

public class SMSMessageSender implements MessageSender {
    @Override
    public void sendMessage(String message) {
        System.out.println("📱 Sending SMS: " + message);
    }
}

