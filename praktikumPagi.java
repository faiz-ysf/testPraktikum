import java.time.LocalDateTime;


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