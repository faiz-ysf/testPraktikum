import java.util.Scanner;


public class TP4 {
	public Scanner input = new Scanner(System.in);	
	boolean statusGame = true;

	public int[][] mutualBook;
	public static void main(String[] args) {

		TP4 fess = new TP4();

		User[] database = fess.daftarUser();
		fess.mutualBook = new int[database.length][database.length];

		do {
		User currentSession = fess.login(database);
		fess.pilihanMenu(currentSession, 
						 database, 
						 currentSession.getMenfessData());
		} while(fess.statusGame);

		
	}

	public User[] daftarUser() {
		System.out.print("Berapa user yang ingin didaftarkan? ");
		int totalUser = Integer.parseInt(input.nextLine());
		User[] newDatabase = new User[totalUser];


		for (int i = 0; i < totalUser; i++) {
			String roleUser;
			
			
			do {
				System.out.print("Masukkan role user dengan ID " + i + " (Admin/Cosmic) : ");
				roleUser = input.nextLine();
				
				if (!roleUser.equalsIgnoreCase("admin") && !roleUser.equalsIgnoreCase("cosmic")) {
					System.out.println("Role tidak valid! Harap masukkan 'Admin' atau 'Cosmic'.");
				}
			} while (!roleUser.equalsIgnoreCase("admin") && !roleUser.equalsIgnoreCase("cosmic"));
			

			System.out.print("Masukkan username user dengan ID " + i + " : ");
			String usernameInput = input.nextLine(); 
			System.out.print("Masukkan password user dengan ID " + i + " : ");
			String passwordInput = input.nextLine();
			
			
			if (roleUser.equalsIgnoreCase("admin")) {
				User tambahUser = new Admin(usernameInput, passwordInput, i);
				newDatabase[i] = tambahUser;
			} else {
				User tambahUser = new Cosmic(usernameInput, passwordInput, i);
				newDatabase[i] = tambahUser;
			}
		}
		
		System.out.println("Telah dibuat " + totalUser + " user dengan masing-masing ID dan username: ");
		
		for (int j = 0; j < totalUser; j++) {
			System.out.println(j + ". " + newDatabase[j].getUsername());
		}
		
		return newDatabase;
	}

	public User login(User[] database) {
		User currentUser = null;
		System.out.println("                ");
		System.out.println("silakan login untuk dapat menggunakan fitur kami");
		
		do {
			System.out.print("Username: "); String username = input.nextLine();
			System.out.print("Password: "); String password = input.nextLine();
			System.out.println("                ");
			for (User user : database) {
				if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
					currentUser = user;
					break;
				}
			}
			if (currentUser != null) {
				System.out.println("Berhasil login!");
				break;
			}
			System.out.println("Maaf, username atau password yang anda masukkan salah. Tolong masukkan kembali dengan benar.");
			
		} while (true);
		return currentUser;
	}

	public void pilihanMenu(User user, User[] database, Menfess[] menfess) {
		String burhanASCII = """
                 .-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-.
                 |        ██████╗ ██╗   ██╗██████╗ ██╗  ██╗ █████╗ ███╗   ██╗            |
                 |        ██╔══██╗██║   ██║██╔══██╗██║  ██║██╔══██╗████╗  ██║            |
                 |        ██████╔╝██║   ██║██████╔╝███████║███████║██╔██╗ ██║            |
                 |        ██╔══██╗██║   ██║██╔══██╗██╔══██║██╔══██║██║╚██╗██║            |
                 |        ██████╔╝╚██████╔╝██║  ██║██║  ██║██║  ██║██║ ╚████║            |
                 |        ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝            |
                 |        ███████╗███████╗███████╗███████╗                               |
                 |        ██╔════╝██╔════╝██╔════╝██╔════╝                               |
                 |        █████╗  █████╗  ███████╗███████╗    + New Refactoring          |                
                 |        ██╔══╝  ██╔══╝  ╚════██║╚════██║      and implementation of    |
                 |        ██║     ███████╗███████║███████║      inheritance &            |
                 |        ╚═╝     ╚══════╝╚══════╝╚══════╝      Polymorphism             |
                 |        ██████╗     ██████╗                                            |
                 |         ════██╗   ██╔═████╗                                           |
                 |         █████╔╝   ██║██╔██║                                           |
                 |         ╚═══██╗   ████╔╝██║                                           |
                 |        ██████╔╝██╗╚██████╔╝                                           |
                 |        ╚═════╝ ╚═╝ ╚═════╝                                            |
                 `-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-'
		""";
		if (user instanceof Admin admin) {
			String menuAdmin = """
				You are Log in as: Admin (Role)
				<%%%%|==========> 1.) Melihat Daftar Pengguna                    <==========|%%%%> 
				<%%%%|==========> 2.) Reset Password Pengguna                    <==========|%%%%>  
				<%%%%|==========> 3.) Sembunyikan Menfess                        <==========|%%%%> 
				<%%%%|==========> 4.) Tampilkan Kembali Menfess                  <==========|%%%%>
				<%%%%|==========> 5.) Logout                                     <==========|%%%%> 
				<%%%%|==========> 6.) End Game                                   <==========|%%%%>  
			""";
			adminLoop:
			while (true) {
				System.out.println(burhanASCII);
				System.out.println(menuAdmin);
				System.out.print("Masukkan pilihanmu ~ ");
				int inputPilihan = Integer.parseInt(input.nextLine());
				switch (inputPilihan) {
					case 1 -> admin.daftarPengguna(database);
					case 2 -> admin.resetPassword(database);
					case 3 -> admin.sembunyikankanKembaliFess(database);
					case 4 -> admin.tampilkanKembaliFess(database);
					case 5 -> { break adminLoop; }
					case 6 -> { statusGame = false; break adminLoop; }
					default -> System.out.println("Pilihan tidak valid. Silakan coba lagi.");
				}
			}
		} else if (user instanceof Cosmic cosmic) {
			String menuCosmic = """
				You are Log in as: Cosmic (Role)
				<%%%%|==========> 1.) Follow Pengguna Lain                       <==========|%%%%> 
				<%%%%|==========> 2.) Mengirim Menfess                           <==========|%%%%>  
				<%%%%|==========> 3.) Melihat Daftar Followers & Following       <==========|%%%%> 
				<%%%%|==========> 4.) Melihat Daftar Menfess                     <==========|%%%%>
				<%%%%|==========> 5.) Mengubah Password                          <==========|%%%%> 
				<%%%%|==========> 6.) Logout                                     <==========|%%%%> 
				<%%%%|==========> 7.) End Game                                   <==========|%%%%> 
			""";
			cosmicLoop:
			while (true) {
				System.out.println(burhanASCII);
				System.out.println(menuCosmic);
				System.out.print("Masukkan pilihanmu ~ ");
				int inputPilihan = Integer.parseInt(input.nextLine());
				switch (inputPilihan) {
					case 1 -> cosmic.followUser(user, database,mutualBook);
					case 2 -> cosmic.kirimFess(user, menfess, database);
					case 3 -> cosmic.seeMutuals(user, database, mutualBook);
					case 4 -> cosmic.lihatSemuaFessPublik(database, user);
					case 5 -> cosmic.changePassword(cosmic);
					case 6 -> { break cosmicLoop; }
					case 7 -> { statusGame = false; break cosmicLoop; }
					default -> System.out.println("Pilihan tidak valid. Silakan coba lagi.");
				}
			}
		}
	}

}