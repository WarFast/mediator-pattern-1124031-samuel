public class Main {
    public static void main(String[] args) {
        ChatRoom general = new ChatRoom("General");
        ChatRoom sports = new ChatRoom("Sports");
        general.addBadWord("bodoh");

        User budi = new User("Budi");
        User andi = new User("Andi");
        User cici = new User("Cici");

        // General: Budi admin
        general.registerUser(budi);
        general.registerUser(andi);
        general.registerUser(cici);

        // Sports: Cici admin, Andi ikut juga (satu user di banyak room)
        sports.registerUser(cici);
        sports.registerUser(andi);

        System.out.println();
        budi.send(general, "Halo semuanya!");
        System.out.println();
        andi.send(sports, "Ada yang nonton bola malam ini?");

        System.out.println();
        System.out.println("--- Andi mencoba kick Cici (bukan admin) ---");
        general.kick(andi, cici);

        System.out.println();
        System.out.println("--- Budi (admin General) kick Cici ---");
        general.kick(budi, cici);

        System.out.println();
        cici.send(general, "Kok aku dikeluarkan?");   // ditolak
        cici.send(sports, "Di Sports aku masih bisa kirim pesan.");
    }
}
