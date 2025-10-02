package structural.bridgepattern;

public class EmailMessageSender implements MessageSender {
    @Override
    public void sendMessage(String message) {
        System.out.println("📧 Sending Email: " + message);
    }
}

