public class Main {
    public static void main(String[] args) {
        ChatRoom general = new ChatRoom("General");
        general.addBadWord("bodoh");

        User budi = new User("Budi");
        User andi = new User("Andi");
        User cici = new User("Cici");

        general.registerUser(budi); // admin
        general.registerUser(andi);
        general.registerUser(cici);

        System.out.println();
        budi.send(general, "Halo semuanya!");
        System.out.println();
        andi.send(general, "Halo Budi, jangan bodoh ya!");

        System.out.println();
        System.out.println("--- Budi mem-mute Andi ---");
        general.muteUser(budi, andi);

        System.out.println();
        andi.send(general, "Kok pesanku nggak muncul?");
        andi.sendPrivate(general, "Cici", "Cici, kamu lihat?");
        budi.send(general, "Andi masih bisa baca pesan ini.");

        System.out.println();
        System.out.println("--- Budi meng-unmute Andi ---");
        general.unmuteUser(budi, andi);
        andi.send(general, "Terima kasih!");

        System.out.println();
        general.showHistory();
    }
}
