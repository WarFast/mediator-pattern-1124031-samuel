public class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Kirim langsung ke user lain (tight coupling)
    public void sendMessage(User receiver, String message) {
        System.out.println("[" + name + " -> " + receiver.getName() + "]: " + message);
        receiver.receiveMessage(this, message);
    }

    public void receiveMessage(User sender, String message) {
        System.out.println("[" + name + "] Menerima pesan dari " + sender.getName() + ": " + message);
    }
}
