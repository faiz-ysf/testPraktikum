import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Cosmic extends User {

	public Scanner input = new Scanner(System.in);
	
	
	public Cosmic(String user, String pass, int id) {
		super(user, pass, id);
	}

	public void changePassword(User user){
    System.out.print("Masukkan password baru: ");
    String newPassword = input.nextLine();
    
    if (user.getPassword().equals(newPassword)) {
        System.out.println("Password baru tidak boleh sama dengan password lama.");
    } else if (newPassword.isEmpty()) {
        System.out.println("Password tidak boleh kosong.");
    } else {
        user.setPassword(newPassword);
        System.out.println("Password berhasil diubah!"); // FIX: Add success message
    	}
	}
	
	public void lihatSemuaFessPublik(User[] database, User user){

		System.out.println("Pilih salah satu daftar menfess!");
		System.out.print("1. Semua Menfess \n2. Menfess Buat Kamu \n Masukkan pilihanmu: ");
		
		fessLoop:
		do {
    switch (Integer.parseInt(input.nextLine())) {
        case 1 -> {
            System.out.println("== Semua Menfess Publik yang Sudah Terkirim == ");
            LocalDateTime currentTime = LocalDateTime.now(); // Move outside loops for efficiency
            boolean hasVisibleMenfess = false; // Track if any menfess are shown
            
            for (User pengguna : database) {
                if (pengguna.getMenfessData() == null) continue;
                for (Menfess pesan : pengguna.getMenfessData()) {
                    if (pesan == null) break; // Only break inner loop, continue with next user

                    if (!pesan.getIsHidden() && !pesan.waktu.isAfter(currentTime)) { 
                        hasVisibleMenfess = true; // Found at least one visible menfess
                        
                        if (pesan instanceof CurhatFess curhatFess){
                            System.out.println("[" + curhatFess.getTipeFess() + "]");
                            System.out.println(" oleh " + pengguna.getUsername() + ": " + curhatFess.displayFess());
                        } else if (pesan instanceof ConfessFess confessFess){
                            System.out.println("[" + confessFess.getTipeFess() + "]");
                            System.out.println(" oleh " + pengguna.getUsername() + ": " + confessFess.displayFess());
                        } else if (pesan instanceof PromosiFess promosiFess){
                            System.out.println("[" + promosiFess.getTipeFess() + "]");
                            System.out.println(" oleh " + pengguna.getUsername() + ": " + promosiFess.displayFess());
                        }
                        System.out.println("---"); 
							}
						}
					}
					
					// Only show "sepi" message if no menfess were actually displayed
					if (!hasVisibleMenfess) {
						System.out.println("Menfess masih sepi.....");
					}
					break fessLoop;
				}
				case 2 -> {
					System.out.println("==Semua Menfess Confession untuk Kamu==");
					LocalDateTime currentTime = LocalDateTime.now();
					
					for (User pengguna : database) {
						Menfess[] userMenfess = pengguna.getMenfessData();
						if (userMenfess == null) continue; // Skip if user has no menfess array
						
						for (Menfess pesan : userMenfess) {
							if (pesan == null) break; // Stop at first null menfess
							
							if (pesan instanceof ConfessFess confessFess && 
								confessFess.getReceiver() != null &&
								confessFess.getReceiver().getUsername().equals(user.getUsername()) &&
								!pesan.getIsHidden() && // Check if not hidden
								(pesan.waktu.isBefore(currentTime) || pesan.waktu.isEqual(currentTime))) { // Check if time has passed
								
								System.out.println("[" + confessFess.getTipeFess() + "]");
								System.out.println(" oleh " + pengguna.getUsername() + ": " + confessFess.displayFess());
								System.out.println("Waktu Kirim: " + 
									confessFess.waktu.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss")));
								System.out.println("---");
							}
						}
					}
					
		
					break fessLoop;
				}
				default -> {
					System.out.println("Pilihan tidak valid. Silakan coba lagi.");
					break;
				}
			}
		} while (true);



	}

	public void kirimFess(User user, Menfess[] menfess, User[] database){

    System.out.print("Berapa fess yang ingin dijadwalkan? (maks 5): ");
    int jumlahLoop;
    while (true) {
        if (input.hasNextInt()) {
            jumlahLoop = input.nextInt();
            input.nextLine();
            if (jumlahLoop >= 1 && jumlahLoop <= 5) {
                break;
            } else {
                System.out.print("Input harus antara 1 dan 5. Coba lagi: ");
            }
        } else {
            System.out.print("Input salah! Masukkan angka: ");
            input.next();
        }
    }

    int[] delayLoops = new int[jumlahLoop];
    Menfess[] fessBaru = new Menfess[jumlahLoop];
    
    // FIX: Get current time once at the start
    LocalDateTime baseTime = LocalDateTime.now();
    
    for (int i = 0; i < jumlahLoop; i++) {
        System.out.print("Masukkan delay fess #" + (i + 1) + " (detik): ");
        while (true) {
            if (input.hasNextInt()) {
                delayLoops[i] = input.nextInt();
                input.nextLine();
                break;
            } else {
                System.out.print("Input salah! Masukkan angka: ");
                input.next();
            }
        }
        
        System.out.print("Masukkan pesan fess #" + (i + 1) + ":  "); 
        String createText = input.nextLine();
        System.out.print("Masukkan tipe fess (confession/promo/curhat): "); 
        String tipeFess = input.nextLine();
        
        User receiverUser = null;
        if (tipeFess.equalsIgnoreCase("confession")){
            System.out.print("Isi username penerima menfess: ");
            while (true) {
                String receiver = input.nextLine();
                for (User penerima : database) {
                    if (penerima.getUsername().equals(receiver)) {
                        receiverUser = penerima;
                        break;
                    }
                }
                if (receiverUser != null) break;
                System.out.println("Mohon tuliskan penerima dengan benar!");
            }
        }
        
        // FIX: Use baseTime consistently
        LocalDateTime waktuKirim = baseTime.plusSeconds(delayLoops[i]);
        
        if (tipeFess.equalsIgnoreCase("confession")) {
            fessBaru[i] = new ConfessFess(user, waktuKirim, createText, receiverUser);
        } else if (tipeFess.equalsIgnoreCase("promo")) {
            fessBaru[i] = new PromosiFess(user, waktuKirim, createText);
        } else if (tipeFess.equalsIgnoreCase("curhat")) {
            fessBaru[i] = new CurhatFess(user, waktuKirim, createText);
        }
        
        // FIX: Use baseTime for display calculation
        System.out.println(delayKirim(delayLoops[i], 
                            baseTime.getSecond(), 
                            baseTime.getMinute(), 
                            baseTime.getHour(), 
                            baseTime.getDayOfMonth(), 
                            baseTime.getMonthValue(), 
                            baseTime.getYear()));
    }

		// Sort delays ascending
		for (int i = 0; i < jumlahLoop - 1; i++) {
			int minIndex = i;
			for (int j = i + 1; j < jumlahLoop; j++) {
				if (delayLoops[j] < delayLoops[minIndex]) {
					minIndex = j;
				}
			}
			int temp = delayLoops[minIndex];
			delayLoops[minIndex] = delayLoops[i];
			delayLoops[i] = temp;
			Menfess tempFess = fessBaru[minIndex];
			fessBaru[minIndex] = fessBaru[i];
			fessBaru[i] = tempFess;
		}

		for (Menfess fess : fessBaru) {
			user.addmenfess(fess);
		}

		System.out.println("\nMengurutkan dan mengirimkan fess... (Sambil memandangi langit)");
		for (Menfess fess : fessBaru) {
			System.out.println("Fess akan dikirim pada: " + fess.waktu.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss")));
			System.out.println("Dengan pesan: " + fess.displayFess());
			System.out.println("Tipe: " + fess.getTipeFess());

			if (fess instanceof ConfessFess confessFess) {
				System.out.println("Kepada: " + confessFess.getReceiver().getUsername());
			}
		}
	}

	public void followUser(User user, User[] database, int[][] mutualBook){
		System.out.print("Masukkan username yang ingin di-follow: ");
		String userToFollow = input.nextLine();
		
		if (user.getUsername().equalsIgnoreCase(userToFollow)) {
			System.out.println("Tidak bisa mem-follow diri sendiri!");
			return;
		}
		
		int currentUserIndex = -1, targetUserIndex = -1;
		
		for (int i = 0; i < database.length; i++) {
			if (database[i].getUsername().equalsIgnoreCase(user.getUsername())) {
				currentUserIndex = i;
			}
			if (database[i].getUsername().equalsIgnoreCase(userToFollow)) {
				targetUserIndex = i;
			}
		}
		
		if (targetUserIndex == -1) {
			System.out.println("Username tidak ditemukan!");
			return;
		}
		
		if (mutualBook[currentUserIndex][targetUserIndex] == 1) {
			System.out.println("Anda sudah mem-follow " + userToFollow + "!");
			return;
		}
		
		mutualBook[currentUserIndex][targetUserIndex] = 1;
		System.out.println("Berhasil mem-follow " + userToFollow + "!");
	}
	
	public void seeMutuals(User user, User[] database, int[][] mutualBook){
		System.out.println("Daftar mutuals BurhanFess");
    for (int i = 0; i < database.length; i++) {
        if (database[i] instanceof Cosmic) {
            System.out.println("== User: " + database[i].getUsername() + ", ID: " + database[i].getId() + " ==");
            printMutuals(i, database, mutualBook);
            System.out.println();
        }
    }
	}

	public void printMutuals(int userIndex, User[] database, int[][] mutualBook){
		System.out.println("Follower: ");
		boolean statusFollower = false;
		
		for (int k = 0; k < database.length; k++) {
			if (k != userIndex && mutualBook[k][userIndex] == 1) {
				System.out.println("- " + database[k].getUsername());
				statusFollower = true;
			}
		}
		if (!statusFollower) {
			System.out.println("Pengguna belum di-follow siapapun");
		}
		
		System.out.println("Following: ");
		boolean statusFollowing = false;
		int[] followingId = mutualBook[userIndex];
		for (int l = 0; l < database.length; l++) {
			if (followingId[l] != 0) {
				System.out.println("- " + database[l].getUsername());
				statusFollowing = true;
			}
		}
		if (!statusFollowing) {
			System.out.println("Pengguna belum mem-follow siapapun");
		}
		
	}

	public String delayKirim(int detikTambahan, int detik, int menit, int jam, int tanggal, int bulan, int tahun){
		LocalDateTime waktuSekarang = LocalDateTime.of(tahun, bulan, tanggal, jam, menit, detik);
		LocalDateTime waktuKirim = waktuSekarang.plusSeconds(detikTambahan);
		String[] namaBulan = {"Januari", "Februari", "Maret", "April", "Mei", 
							  "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
		int bulanKirim = waktuKirim.getMonthValue();
		return "Fess akan dikirimkan pada: " + 
		waktuKirim.getDayOfMonth() + " " + 
		namaBulan[bulanKirim - 1] + " " + 
		waktuKirim.getYear() + ", pukul " + 
		String.format("%02d:%02d:%02d", 
						waktuKirim.getHour(), 
						waktuKirim.getMinute(), 
						waktuKirim.getSecond());
}

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
	@Override
	public String getRole(){
		return "Cosmic";
	}
}