import java.util.Scanner;

public class Admin extends User  {	
	public Scanner input = new Scanner(System.in);
	
	public Admin (String username, String password, int id){
		super(username, password, id);
	}
	
	public void daftarPengguna(User[] user){
		System.out.println(".-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-.");
		System.out.println("| ID | Username                       |  Role                           |");
		System.out.println("*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*");
		for (User pengguna : user) {
			System.out.printf("| %-2d | %-30s | %-30s |\n", pengguna.getId(), pengguna.getUsername(), pengguna.getRole());
		}
		System.out.println("`-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-'");
	}
	
	public void tampilkanKembaliFess(User[] user){
		System.out.println("== Semua Menfess yang Disembunyikan == ");
		boolean foundHiddenFess = false;
		
		for (User pengguna : user) {
			if (pengguna.getMenfessData() != null) {
				for (int i = 0; i < pengguna.getMenfessData().length; i++) {
					Menfess fess = pengguna.getMenfessData()[i];
					if (fess != null && fess.getIsHidden()) {
						System.out.println("[" + pengguna.getUsername() + "] " + 
										"[" + i + "]" + 
										"[" + fess.getTipeFess() + "] " +
										"oleh " + pengguna.getUsername() + ": " +
										fess.displayFess());
						foundHiddenFess = true;
					}
				}
			}
		}
		
		if (!foundHiddenFess) {
			System.out.println("Tidak ada menfess yang disembunyikan.");
			return;
		}
		
		System.out.println("Masukkan username pemilik fess: "); 
		String username = input.nextLine();
		
		// FIX: Input validation without try-catch
		System.out.println("Masukkan index fess yang ingin dikembalikan : ");
		int index;
		while (true) {
			if (input.hasNextInt()) {
				index = input.nextInt();
				input.nextLine(); // consume newline
				break;
			} else {
				System.out.print("Input harus berupa angka. Silakan coba lagi: ");
				input.next(); // consume invalid input
			}
		}
		
		// FIX: Find user and validate array bounds
		User targetUser = null;
		for (User pengguna : user) {
			if (pengguna.getUsername().equals(username)) {
				targetUser = pengguna;
				break;
			}
		}
		
		if (targetUser == null) {
			System.out.println("Username tidak ditemukan.");
			return;
		}
		
		Menfess[] userMenfess = targetUser.getMenfessData();
		
		// FIX: Comprehensive bounds and validity checking
		if (userMenfess == null) {
			System.out.println("User tidak memiliki data menfess.");
			return;
		}
		
		if (index < 0 || index >= userMenfess.length) {
			System.out.println("Index fess tidak valid. Index harus antara 0 dan " + (userMenfess.length - 1));
			return;
		}
		
		if (userMenfess[index] == null) {
			System.out.println("Tidak ada menfess pada index tersebut.");
			return;
		}
		
		if (!userMenfess[index].getIsHidden()) {
			System.out.println("Menfess pada index tersebut tidak disembunyikan.");
			return;
		}
		
		// If all validations pass, unhide the menfess
		userMenfess[index].unhide();
		System.out.println("Fess berhasil dikembalikan.");
}

    public void sembunyikankanKembaliFess(User[] user){
    System.out.println("== Semua Menfess Publik yang Terlihat == ");
    boolean foundVisibleFess = false;
    
    for (User pengguna : user) {
			if (pengguna.getMenfessData() != null) {
				for (int i = 0; i < pengguna.getMenfessData().length; i++) {
					Menfess fess = pengguna.getMenfessData()[i];
					if (fess != null && !fess.getIsHidden()) {
						System.out.println("[" + pengguna.getUsername() + "] " + 
										"[" + i + "]" + 
										"[" + fess.getTipeFess() + "] " +
										"oleh " + pengguna.getUsername() + ": " +
										fess.displayFess());
						foundVisibleFess = true;
					}
				}
			}
		}
		
		if (!foundVisibleFess) {
			System.out.println("Tidak ada menfess yang terlihat.");
			return;
		}
		
		System.out.println("Masukkan username pemilik fess: ");
		String username = input.nextLine();
		
		// FIX: Input validation without try-catch
		System.out.println("Masukkan index fess yang ingin disembunyikan: ");
		int index;
		while (true) {
			if (input.hasNextInt()) {
				index = input.nextInt();
				input.nextLine(); // consume newline
				break;
			} else {
				System.out.print("Input harus berupa angka. Silakan coba lagi: ");
				input.next(); // consume invalid input
			}
		}
		
		// FIX: Find user and validate array bounds
		User targetUser = null;
		for (User pengguna : user) {
			if (pengguna.getUsername().equals(username)) {
				targetUser = pengguna;
				break;
			}
		}
		
		if (targetUser == null) {
			System.out.println("Username tidak ditemukan.");
			return;
		}
		
		Menfess[] userMenfess = targetUser.getMenfessData();
		
		// FIX: Comprehensive bounds and validity checking
		if (userMenfess == null) {
			System.out.println("User tidak memiliki data menfess.");
			return;
		}
		
		if (index < 0 || index >= userMenfess.length) {
			System.out.println("Index fess tidak valid. Index harus antara 0 dan " + (userMenfess.length - 1));
			return;
		}
		
		if (userMenfess[index] == null) {
			System.out.println("Tidak ada menfess pada index tersebut.");
			return;
		}
		
		if (userMenfess[index].getIsHidden()) {
			System.out.println("Menfess pada index tersebut sudah disembunyikan.");
			return;
		}
		
		// If all validations pass, hide the menfess
		userMenfess[index].hide();
		System.out.println("Fess berhasil disembunyikan.");
	}
	
	public void resetPassword(User[] user){
		User targetUser = null; 
		
		findUser:
		do {
			System.out.print("Masukkan Username: ");
			String inputUser = input.nextLine();
			for (User target : user){
				if (target.getUsername().equals(inputUser)) {
					targetUser = target;
					break findUser;
				}
			}

			System.out.println("Maaf, username yang anda masukkan salah." + 
							"Tolong masukkan kembali dengan benar.");
		} while(true);


		do { 
			System.out.print("Masukkan password baru: ");
			String newPassword = input.nextLine();
			
			if (newPassword.isEmpty()) {
				System.out.println("Password tidak boleh kosong.");
			} else if (targetUser.getPassword().equals(newPassword)) {
				System.out.println("Password baru tidak boleh sama dengan password lama.");
			} else {
				targetUser.setPassword(newPassword);
				System.out.println("Password berhasil diubah.");
				break;
			}
		} while (true);
	}

	@Override
	public String getRole() {
		return "admin";
	}
	
}