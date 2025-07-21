import java.time.LocalDateTime;

public class ConfessFess extends Menfess {
    
    private User penerima;
    private String[] replies; // Array to store replies
    private int replyCount; // Track number of replies
    private static final int MAX_REPLIES = 50; // Maximum replies per confession
    
    public ConfessFess(User user, LocalDateTime waktu, String isiMenfess, User penerima){
        super(user, waktu, isiMenfess);
        this.penerima = penerima;
        this.replies = new String[MAX_REPLIES];
        this.replyCount = 0;
    }

    public User getReceiver() {
        return penerima;
    }
    
    public void addReply(String reply) {
        if (replyCount < MAX_REPLIES) {
            replies[replyCount] = reply;
            replyCount++;
        }
    }
    
    public String[] getReplies() {
        return replies;
    }
    
    public int getReplyCount() {
        return replyCount;
    }
    
    public boolean hasReplies() {
        return replyCount > 0;
    }

    @Override
    public String displayFess() {
        return isiMenfess; 
    }
    
    @Override
    public String getTipeFess() {
        return "confession";
    }
}

// Add these methods to the Cosmic class

public void lihatConfessionUntukSaya(User[] database, User user) {
    System.out.println("== Confession yang Ditujukan untuk Anda ==");
    boolean foundConfession = false;
    LocalDateTime currentTime = LocalDateTime.now();
    
    for (User pengguna : database) {
        if (pengguna.getMenfessData() == null) continue;
        
        for (int i = 0; i < pengguna.getMenfessData().length; i++) {
            Menfess pesan = pengguna.getMenfessData()[i];
            if (pesan == null) break;
            
            if (pesan instanceof ConfessFess confessFess && 
                confessFess.getReceiver() != null &&
                confessFess.getReceiver().getUsername().equals(user.getUsername()) &&
                !pesan.getIsHidden() && 
                (pesan.waktu.isBefore(currentTime) || pesan.waktu.isEqual(currentTime))) {
                
                foundConfession = true;
                System.out.println("=== Confession #" + i + " ===");
                System.out.println("Dari: [ANONIM]");
                System.out.println("Pesan: " + confessFess.displayFess());
                System.out.println("Waktu: " + confessFess.waktu.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss")));
                
                if (confessFess.hasReplies()) {
                    System.out.println("Balasan Anda:");
                    for (int j = 0; j < confessFess.getReplyCount(); j++) {
                        System.out.println("  -> " + confessFess.getReplies()[j]);
                    }
                }
                System.out.println("---");
            }
        }
    }
    
    if (!foundConfession) {
        System.out.println("Tidak ada confession untuk Anda.");
        return;
    }
    
    System.out.print("Ingin membalas confession? (y/n): ");
    String choice = input.nextLine();
    
    if (choice.equalsIgnoreCase("y")) {
        balasConfession(database, user);
    }
}

public void balasConfession(User[] database, User user) {
    System.out.print("Masukkan nomor confession yang ingin dibalas: ");
    int confessionIndex;
    
    while (true) {
        if (input.hasNextInt()) {
            confessionIndex = input.nextInt();
            input.nextLine(); // consume newline
            break;
        } else {
            System.out.print("Input harus berupa angka. Silakan coba lagi: ");
            input.next();
        }
    }
    
    // Find the confession
    ConfessFess targetConfession = null;
    for (User pengguna : database) {
        if (pengguna.getMenfessData() == null) continue;
        
        if (confessionIndex >= 0 && confessionIndex < pengguna.getMenfessData().length) {
            Menfess pesan = pengguna.getMenfessData()[confessionIndex];
            if (pesan instanceof ConfessFess confessFess && 
                confessFess.getReceiver() != null &&
                confessFess.getReceiver().getUsername().equals(user.getUsername()) &&
                !pesan.getIsHidden()) {
                targetConfession = confessFess;
                break;
            }
        }
    }
    
    if (targetConfession == null) {
        System.out.println("Confession tidak ditemukan atau bukan untuk Anda.");
        return;
    }
    
    System.out.print("Masukkan balasan Anda: ");
    String reply = input.nextLine();
    
    if (reply.trim().isEmpty()) {
        System.out.println("Balasan tidak boleh kosong.");
        return;
    }
    
    targetConfession.addReply(reply);
    System.out.println("Balasan berhasil dikirim secara anonim!");
}

public void lihatBalasanConfessionSaya(User user) {
    System.out.println("== Balasan untuk Confession yang Anda Kirim ==");
    boolean foundRepliedConfession = false;
    
    if (user.getMenfessData() == null) {
        System.out.println("Anda belum mengirim confession apapun.");
        return;
    }
    
    for (int i = 0; i < user.getMenfessData().length; i++) {
        Menfess pesan = user.getMenfessData()[i];
        if (pesan == null) break;
        
        if (pesan instanceof ConfessFess confessFess && confessFess.hasReplies()) {
            foundRepliedConfession = true;
            System.out.println("=== Confession #" + i + " ===");
            System.out.println("Kepada: " + confessFess.getReceiver().getUsername());
            System.out.println("Pesan Anda: " + confessFess.displayFess());
            System.out.println("Waktu Kirim: " + confessFess.waktu.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss")));
            System.out.println("Balasan dari penerima:");
            
            for (int j = 0; j < confessFess.getReplyCount(); j++) {
                System.out.println("  -> " + confessFess.getReplies()[j]);
            }
            System.out.println("---");
        }
    }
    
    if (!foundRepliedConfession) {
        System.out.println("Belum ada balasan untuk confession yang Anda kirim.");
    }
}

// Modify the pilihanMenu method in TP4 class for Cosmic users

else if (user instanceof Cosmic cosmic) {
    String menuCosmic = """
        You are Log in as: Cosmic (Role)
        <%%%%|==========> 1.) Follow Pengguna Lain                       <==========|%%%%> 
        <%%%%|==========> 2.) Mengirim Menfess                           <==========|%%%%>  
        <%%%%|==========> 3.) Melihat Daftar Followers & Following       <==========|%%%%> 
        <%%%%|==========> 4.) Melihat Daftar Menfess                     <==========|%%%%>
        <%%%%|==========> 5.) Melihat & Balas Confession untuk Anda      <==========|%%%%>
        <%%%%|==========> 6.) Lihat Balasan Confession yang Anda Kirim   <==========|%%%%>
        <%%%%|==========> 7.) Mengubah Password                          <==========|%%%%> 
        <%%%%|==========> 8.) Logout                                     <==========|%%%%> 
        <%%%%|==========> 9.) End Game                                   <==========|%%%%> 
    """;
    cosmicLoop:
    while (true) {
        System.out.println(burhanASCII);
        System.out.println(menuCosmic);
        System.out.print("Masukkan pilihanmu ~ ");
        int inputPilihan = Integer.parseInt(input.nextLine());
        switch (inputPilihan) {
            case 1 -> cosmic.followUser(user, database, mutualBook);
            case 2 -> cosmic.kirimFess(user, menfess, database);
            case 3 -> cosmic.seeMutuals(user, database, mutualBook);
            case 4 -> cosmic.lihatSemuaFessPublik(database, user);
            case 5 -> cosmic.lihatConfessionUntukSaya(database, user);
            case 6 -> cosmic.lihatBalasanConfessionSaya(user);
            case 7 -> cosmic.changePassword(cosmic);
            case 8 -> { break cosmicLoop; }
            case 9 -> { statusGame = false; break cosmicLoop; }
            default -> System.out.println("Pilihan tidak valid. Silakan coba lagi.");
        }
    }
}