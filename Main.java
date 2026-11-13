public class Main {
    public static void main(String[] args) {
        ChatRoom general = new ChatRoom("General");
        general.addBadWord("anjing");
        general.addBadWord("bodoh");

        User budi = new User("Budi");
        User andi = new User("Andi");
        User cici = new User("Cici");

        general.registerUser(budi);
        general.registerUser(andi);
        general.registerUser(cici);

        budi.send("Halo semuanya!");
        System.out.println();
        andi.send("Dasar bodoh kamu, Anjing!");
        System.out.println();
        budi.sendPrivate("Cici", "Jangan bodoh ya, ini rahasia");
    }
}
