import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatRoom {
    private String roomName;
    private List<User> members;
    private List<String> badWords;

    public ChatRoom(String roomName) {
        this.roomName = roomName;
        this.members = new ArrayList<>();
        this.badWords = new ArrayList<>();
    }

    public String getRoomName() {
        return roomName;
    }

    public void registerUser(User user) {
        if (!members.contains(user)) {
            members.add(user);
            user.setChatRoom(this);
        }
    }

    public void addBadWord(String word) {
        String w = word.toLowerCase();
        if (!badWords.contains(w)) {
            badWords.add(w);
        }
    }

    // Ganti kata terlarang (case-insensitive, kata utuh) dengan ***
    public String filterMessage(String message) {
        String result = message;
        for (String bad : badWords) {
            Pattern p = Pattern.compile("\\b" + Pattern.quote(bad) + "\\b", Pattern.CASE_INSENSITIVE);
            result = p.matcher(result).replaceAll(Matcher.quoteReplacement("***"));
        }
        return result;
    }

    public void sendBroadcast(User sender, String message) {
        String filtered = filterMessage(message);
        System.out.println("[ChatRoom - " + roomName + "] (Broadcast dari " + sender.getName() + "): " + filtered);
        for (User member : members) {
            if (member != sender) {
                member.receiveMessage(sender, filtered);
            }
        }
    }

    public void sendPrivateMessage(User sender, String recipientName, String message) {
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
