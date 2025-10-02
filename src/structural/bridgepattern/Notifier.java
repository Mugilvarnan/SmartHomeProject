package structural.bridgepattern;

public abstract class Notifier {
    protected MessageSender messageSender;  // Bridge: reference to implementor

    public Notifier(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public abstract void notifyUser(String message);
}

