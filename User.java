public class User {
    private String name;
    private ChatRoom chatRoom;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    // User hanya kenal ChatRoom, tidak kenal user lain
    public void send(String message) {
        if (chatRoom == null) {
            System.out.println("[" + name + "]: Belum tergabung di ChatRoom manapun.");
            return;
        }
        System.out.println("[" + name + "]: Memproses pengiriman pesan grup...");
        chatRoom.sendBroadcast(this, message);
    }

    public void receiveMessage(User sender, String message) {
        System.out.println("[" + name + "] Menerima pesan dari " + sender.getName() + ": " + message);
    }
}
