package structural.bridgepattern;

public class AlertNotifier extends Notifier {
    public AlertNotifier(MessageSender messageSender) {
        super(messageSender);
    }

    @Override
    public void notifyUser(String message) {
        System.out.print("🔔 Alert: ");
        messageSender.sendMessage(message);
    }
}

