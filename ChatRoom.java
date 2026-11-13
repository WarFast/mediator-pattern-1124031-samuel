import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatRoom {
    private String roomName;
    private List<User> members;
    private List<String> badWords;
    private User admin;
    private List<User> mutedUsers;   // tahap 7
    private List<String> history;    // tahap 7

    public ChatRoom(String roomName) {
        this.roomName = roomName;
        this.members = new ArrayList<>();
        this.badWords = new ArrayList<>();
        this.mutedUsers = new ArrayList<>();
        this.history = new ArrayList<>();
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

    public void removeUser(User user) {
        if (members.remove(user)) {
            mutedUsers.remove(user);
            user.removeChatRoom(this);
            System.out.println("[ChatRoom - " + roomName + "] " + user.getName() + " telah dikeluarkan.");
        }
    }

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

    // ---------- Mute (tahap 7) ----------
    public void muteUser(User requester, User target) {
        if (requester != admin) {
            System.out.println("[ChatRoom - " + roomName + "] Akses ditolak: " + requester.getName()
                    + " bukan admin.");
            return;
        }
        if (!members.contains(target) || target == admin) {
            System.out.println("[ChatRoom - " + roomName + "] " + target.getName() + " tidak bisa di-mute.");
            return;
        }
        if (!mutedUsers.contains(target)) {
            mutedUsers.add(target);
            System.out.println("[ChatRoom - " + roomName + "] " + target.getName() + " di-mute.");
        }
    }

    public void unmuteUser(User requester, User target) {
        if (requester != admin) {
            System.out.println("[ChatRoom - " + roomName + "] Akses ditolak: " + requester.getName()
                    + " bukan admin.");
            return;
        }
        if (mutedUsers.remove(target)) {
            System.out.println("[ChatRoom - " + roomName + "] " + target.getName() + " di-unmute.");
        }
    }

    public boolean isMuted(User user) {
        return mutedUsers.contains(user);
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

        if (isMuted(sender)) {
            history.add("[Broadcast] " + sender.getName() + ": " + filtered + " (DIBLOKIR - user di-mute)");
            System.out.println("[ChatRoom - " + roomName + "] Pesan dari " + sender.getName()
                    + " tidak disebarkan karena di-mute.");
            return;
        }

        history.add("[Broadcast] " + sender.getName() + ": " + filtered);
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

        if (isMuted(sender)) {
            history.add("[Private " + sender.getName() + " -> " + recipient.getName() + "] " + filtered
                    + " (DIBLOKIR - user di-mute)");
            System.out.println("[ChatRoom - " + roomName + "] Pesan privat dari " + sender.getName()
                    + " tidak disebarkan karena di-mute.");
            return;
        }

        history.add("[Private " + sender.getName() + " -> " + recipient.getName() + "] " + filtered);
        System.out.println("[ChatRoom - " + roomName + "] (Private " + sender.getName() + " -> "
                + recipient.getName() + "): " + filtered);
        recipient.receivePrivateMessage(sender, filtered);
    }

    public void showHistory() {
        System.out.println("===== Riwayat Chat - " + roomName + " =====");
        if (history.isEmpty()) {
            System.out.println("(belum ada pesan)");
        }
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }
        System.out.println("=================================");
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
