import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatRoom {
    private String roomName;
    private List<User> members;
    private List<String> badWords;
    private User admin; // orang pertama yang masuk

    public ChatRoom(String roomName) {
        this.roomName = roomName;
        this.members = new ArrayList<>();
        this.badWords = new ArrayList<>();
    }

    public String getRoomName() {
        return roomName;
    }

    public User getAdmin() {
        return admin;
    }

    public void registerUser(User user) {
        if (members.contains(user)) {
            return;
        }
        members.add(user);
        user.setChatRoom(this);
        if (admin == null) {
            admin = user;
            System.out.println("[ChatRoom - " + roomName + "] " + user.getName() + " bergabung sebagai ADMIN.");
        } else {
            System.out.println("[ChatRoom - " + roomName + "] " + user.getName() + " bergabung.");
        }
    }

    // Kick user dari room (logika inti sesuai spesifikasi)
    public void removeUser(User user) {
        if (members.remove(user)) {
            user.removeChatRoom(this);
            System.out.println("[ChatRoom - " + roomName + "] " + user.getName() + " telah dikeluarkan.");
        }
    }

    // Kick yang divalidasi: hanya admin yang boleh
    public void kick(User requester, User target) {
        if (requester != admin) {
            System.out.println("[ChatRoom - " + roomName + "] Akses ditolak: " + requester.getName()
                    + " bukan admin.");
            return;
        }
        if (target == admin) {
            System.out.println("[ChatRoom - " + roomName + "] Admin tidak bisa mengeluarkan dirinya sendiri.");
            return;
        }
        if (!members.contains(target)) {
            System.out.println("[ChatRoom - " + roomName + "] " + target.getName() + " tidak ada di room ini.");
            return;
        }
        removeUser(target);
    }

    public void addBadWord(String word) {
        String w = word.toLowerCase();
        if (!badWords.contains(w)) {
            badWords.add(w);
        }
    }

    public String filterMessage(String message) {
        String result = message;
        for (String bad : badWords) {
            Pattern p = Pattern.compile("\\b" + Pattern.quote(bad) + "\\b", Pattern.CASE_INSENSITIVE);
            result = p.matcher(result).replaceAll(Matcher.quoteReplacement("***"));
        }
        return result;
    }

    public void sendBroadcast(User sender, String message) {
        if (!members.contains(sender)) {
            System.out.println("[ChatRoom - " + roomName + "] " + sender.getName() + " bukan anggota room ini.");
            return;
        }
        String filtered = filterMessage(message);
        System.out.println("[ChatRoom - " + roomName + "] (Broadcast dari " + sender.getName() + "): " + filtered);
        for (User member : members) {
            if (member != sender) {
                member.receiveMessage(sender, filtered);
            }
        }
    }

    public void sendPrivateMessage(User sender, String recipientName, String message) {
        if (!members.contains(sender)) {
            System.out.println("[ChatRoom - " + roomName + "] " + sender.getName() + " bukan anggota room ini.");
            return;
        }
        User recipient = findMember(recipientName);
        if (recipient == null) {
            System.out.println("[ChatRoom - " + roomName + "] User '" + recipientName + "' tidak ditemukan.");
            return;
        }
        String filtered = filterMessage(message);
        System.out.println("[ChatRoom - " + roomName + "] (Private " + sender.getName() + " -> "
                + recipient.getName() + "): " + filtered);
        recipient.receivePrivateMessage(sender, filtered);
    }

    private User findMember(String name) {
        for (User member : members) {
            if (member.getName().equalsIgnoreCase(name)) {
                return member;
            }
        }
        return null;
    }
}
