import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private List<ChatRoom> chatRooms;

    public User(String name) {
        this.name = name;
        this.chatRooms = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        if (!chatRooms.contains(chatRoom)) {
            chatRooms.add(chatRoom);
        }
    }
    public void removeChatRoom(ChatRoom chatRoom) {
        chatRooms.remove(chatRoom);
    }

    public void send(ChatRoom room, String message) {
        if (!chatRooms.contains(room)) {
            System.out.println("[" + name + "]: Tidak tergabung di ChatRoom " + room.getRoomName() + ".");
            return;
        }
        System.out.println("[" + name + "]: Memproses pengiriman pesan grup...");
        room.sendBroadcast(this, message);
    }

    public void sendPrivate(ChatRoom room, String recipientName, String message) {
        if (!chatRooms.contains(room)) {
            System.out.println("[" + name + "]: Tidak tergabung di ChatRoom " + room.getRoomName() + ".");
            return;
        }
        System.out.println("[" + name + "]: Mengirim pesan privat ke " + recipientName + "...");
        room.sendPrivateMessage(this, recipientName, message);
    }

    public void receiveMessage(User sender, String message) {
        System.out.println("[" + name + "] Menerima pesan dari " + sender.getName() + ": " + message);
    }

    public void receivePrivateMessage(User sender, String message) {
        System.out.println("[" + name + "] Menerima pesan privat dari " + sender.getName() + ": " + message);
    }
}
