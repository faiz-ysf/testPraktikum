import org.junit.jupiter.api.*;
import java.io.*;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class TP4Test {
    private TP4 tp4;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private InputStream originalIn;

    @BeforeEach
    void setUp() {
        tp4 = new TP4();
        
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        originalIn = System.in;
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testDaftarUser_CreateAdminUsers() {
        String input = "2\nadmin\nadminUser1\nadminPass1\nadmin\nadminUser2\nadminPass2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        User[] users = tp4.daftarUser();
        
        assertEquals(2, users.length);
        assertTrue(users[0] instanceof Admin);
        assertTrue(users[1] instanceof Admin);
        assertEquals("adminUser1", users[0].getUsername());
        assertEquals("adminUser2", users[1].getUsername());
        assertEquals("adminPass1", users[0].getPassword());
        assertEquals("adminPass2", users[1].getPassword());
        assertEquals(0, users[0].getId());
        assertEquals(1, users[1].getId());
        
        String output = outputStream.toString();
        assertTrue(output.contains("Telah dibuat 2 user"));
        assertTrue(output.contains("0. adminUser1"));
        assertTrue(output.contains("1. adminUser2"));
    }

    @Test
    void testDaftarUser_CreateCosmicUsers() {
        String input = "2\ncosmic\ncosmicUser1\ncosmicPass1\ncosmic\ncosmicUser2\ncosmicPass2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        User[] users = tp4.daftarUser();
        
        assertEquals(2, users.length);
        assertTrue(users[0] instanceof Cosmic);
        assertTrue(users[1] instanceof Cosmic);
        assertEquals("cosmicUser1", users[0].getUsername());
        assertEquals("cosmicUser2", users[1].getUsername());
        assertEquals("cosmicPass1", users[0].getPassword());
        assertEquals("cosmicPass2", users[1].getPassword());
    }

    @Test
    void testDaftarUser_MixedUserTypes() {
        String input = "3\nadmin\nadminUser\nadminPass\ncosmic\ncosmicUser\ncosmicPass\nadmin\nadmin2\npass2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        User[] users = tp4.daftarUser();
        
        assertEquals(3, users.length);
        assertTrue(users[0] instanceof Admin);
        assertTrue(users[1] instanceof Cosmic);
        assertTrue(users[2] instanceof Admin);
        assertEquals("admin", users[0].getRole());
        assertEquals("Cosmic", users[1].getRole());
        assertEquals("admin", users[2].getRole());
    }

    @Test
    void testDaftarUser_InvalidRoleThenValid() {
        String input = "1\ninvalidRole\nuser\nadmin\nadminUser\nadminPass\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        User[] users = tp4.daftarUser();
        
        assertEquals(1, users.length);
        assertTrue(users[0] instanceof Admin);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Role tidak valid"));
    }

    @Test
    void testDaftarUser_CaseInsensitiveRoles() {
        String input = "2\nADMIN\nadminUser\nadminPass\nCOSMIC\ncosmicUser\ncosmicPass\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        User[] users = tp4.daftarUser();
        
        assertEquals(2, users.length);
        assertTrue(users[0] instanceof Admin);
        assertTrue(users[1] instanceof Cosmic);
    }

    @Test
    void testLogin_SuccessfulLogin() {
        User[] testUsers = createTestUsers();
        
        String input = "adminUser\nadminPass\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        User loggedInUser = tp4.login(testUsers);
        
        assertNotNull(loggedInUser);
        assertEquals("adminUser", loggedInUser.getUsername());
        assertEquals("adminPass", loggedInUser.getPassword());
        assertTrue(loggedInUser instanceof Admin);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Berhasil login"));
    }

    @Test
    void testLogin_WrongUsername() {
        User[] testUsers = createTestUsers();
        
        String input = "wrongUser\nadminPass\nadminUser\nadminPass\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        User loggedInUser = tp4.login(testUsers);
        
        assertNotNull(loggedInUser);
        assertEquals("adminUser", loggedInUser.getUsername());
        
        String output = outputStream.toString();
        assertTrue(output.contains("username atau password yang anda masukkan salah"));
    }

    @Test
    void testLogin_WrongPassword() {
        User[] testUsers = createTestUsers();
        
        String input = "adminUser\nwrongPass\nadminUser\nadminPass\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        User loggedInUser = tp4.login(testUsers);
        
        assertNotNull(loggedInUser);
        assertEquals("adminUser", loggedInUser.getUsername());
        
        String output = outputStream.toString();
        assertTrue(output.contains("username atau password yang anda masukkan salah"));
    }

    @Test
    void testLogin_MultipleWrongAttempts() {
        User[] testUsers = createTestUsers();
        
        String input = "wrong1\nwrong1\nwrong2\nwrong2\nadminUser\nadminPass\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        User loggedInUser = tp4.login(testUsers);
        
        assertNotNull(loggedInUser);
        assertEquals("adminUser", loggedInUser.getUsername());
        
        String output = outputStream.toString();
        // Should contain multiple error messages
        long errorCount = output.lines()
            .filter(line -> line.contains("username atau password yang anda masukkan salah"))
            .count();
        assertEquals(2, errorCount);
    }

    @Test
    void testPilihanMenu_AdminUser_Logout() {
        User[] testUsers = createTestUsers();
        Admin admin = (Admin) testUsers[0];
        tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
        String input = "5\n"; // Logout option
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        tp4.pilihanMenu(admin, testUsers, admin.getMenfessData());
        
        String output = outputStream.toString();
        assertTrue(output.contains("You are Log in as: Admin"));
        assertTrue(output.contains("Melihat Daftar Pengguna"));
        assertTrue(output.contains("Reset Password Pengguna"));
        assertTrue(output.contains("Sembunyikan Menfess"));
        assertTrue(output.contains("Tampilkan Kembali Menfess"));
        assertTrue(output.contains("Logout"));
        assertTrue(output.contains("End Game"));
    }

    @Test
    void testPilihanMenu_AdminUser_EndGame() {
        User[] testUsers = createTestUsers();
        Admin admin = (Admin) testUsers[0];
        tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
        String input = "6\n"; // End Game option
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        tp4.pilihanMenu(admin, testUsers, admin.getMenfessData());
        
        assertFalse(tp4.statusGame);
    }

    @Test
    void testPilihanMenu_AdminUser_DaftarPengguna() {
        User[] testUsers = createTestUsers();
        Admin admin = (Admin) testUsers[0];
        tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
        String input = "1\n5\n"; // View users then logout
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        tp4.pilihanMenu(admin, testUsers, admin.getMenfessData());
        
        String output = outputStream.toString();
        assertTrue(output.contains("adminUser"));
        assertTrue(output.contains("cosmicUser"));
    }

    @Test
    void testPilihanMenu_AdminUser_InvalidOption() {
        User[] testUsers = createTestUsers();
        Admin admin = (Admin) testUsers[0];
        tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
        String input = "99\n5\n"; // Invalid option then logout
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        tp4.pilihanMenu(admin, testUsers, admin.getMenfessData());
        
        String output = outputStream.toString();
        assertTrue(output.contains("Pilihan tidak valid"));
    }

    @Test
    void testPilihanMenu_CosmicUser_Logout() {
        User[] testUsers = createTestUsers();
        Cosmic cosmic = (Cosmic) testUsers[1];
        tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
        String input = "6\n"; // Logout option
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        tp4.pilihanMenu(cosmic, testUsers, cosmic.getMenfessData());
        
        String output = outputStream.toString();
        assertTrue(output.contains("You are Log in as: Cosmic"));
        assertTrue(output.contains("Follow Pengguna Lain"));
        assertTrue(output.contains("Mengirim Menfess"));
        assertTrue(output.contains("Melihat Daftar Followers & Following"));
        assertTrue(output.contains("Melihat Daftar Menfess"));
        assertTrue(output.contains("Mengubah Password"));
        assertTrue(output.contains("Logout"));
        assertTrue(output.contains("End Game"));
    }

    @Test
    void testPilihanMenu_CosmicUser_EndGame() {
        User[] testUsers = createTestUsers();
        Cosmic cosmic = (Cosmic) testUsers[1];
        tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
        String input = "7\n"; // End Game option
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        tp4.pilihanMenu(cosmic, testUsers, cosmic.getMenfessData());
        
        assertFalse(tp4.statusGame);
    }

    // @Test
    // void testPilihanMenu_CosmicUser_FollowUser() {
    //     User[] testUsers = createTestUsers();
    //     Cosmic cosmic = (Cosmic) testUsers[1];
    //     tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
    //     String input = "1\nadminUser\n6\n"; // Follow user then logout
    //     System.setIn(new ByteArrayInputStream(input.getBytes()));
    //     tp4.input = new Scanner(System.in);
        
    //     tp4.pilihanMenu(cosmic, testUsers, cosmic.getMenfessData());
        
    //     String output = outputStream.toString();
    //     assertTrue(output.contains("Berhasil mem-follow adminUser"));
    // }

    // @Test
    // void testPilihanMenu_CosmicUser_ChangePassword() {
    //     User[] testUsers = createTestUsers();
    //     Cosmic cosmic = (Cosmic) testUsers[1];
    //     tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
    //     String input = "5\nnewPassword123\n6\n"; // Change password then logout
    //     System.setIn(new ByteArrayInputStream(input.getBytes()));
    //     tp4.input = new Scanner(System.in);
        
    //     tp4.pilihanMenu(cosmic, testUsers, cosmic.getMenfessData());
        
    //     String output = outputStream.toString();
    //     assertTrue(output.contains("Password berhasil diubah"));
    //     assertEquals("newPassword123", cosmic.getPassword());
    // }

    // @Test
    // void testPilihanMenu_CosmicUser_ViewMenfess() {
    //     User[] testUsers = createTestUsers();
    //     Cosmic cosmic = (Cosmic) testUsers[1];
    //     tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
    //     // Add some test menfess
    //     java.time.LocalDateTime now = java.time.LocalDateTime.now().minusMinutes(1);
    //     cosmic.addmenfess(new CurhatFess(cosmic, now, "Test curhat message"));
        
    //     String input = "4\n1\n6\n"; // View menfess (all) then logout
    //     System.setIn(new ByteArrayInputStream(input.getBytes()));
    //     tp4.input = new Scanner(System.in);
        
    //     tp4.pilihanMenu(cosmic, testUsers, cosmic.getMenfessData());
        
    //     String output = outputStream.toString();
    //     assertTrue(output.contains("Semua Menfess Publik"));
    //     assertTrue(output.contains("Test curhat message"));
    // }

    // @Test
    // void testPilihanMenu_CosmicUser_SendMenfess() {
    //     User[] testUsers = createTestUsers();
    //     Cosmic cosmic = (Cosmic) testUsers[1];
    //     tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
    //     String input = "2\n1\n5\nTest message\ncurhat\n6\n"; // Send menfess then logout
    //     System.setIn(new ByteArrayInputStream(input.getBytes()));
    //     tp4.input = new Scanner(System.in);
        
    //     tp4.pilihanMenu(cosmic, testUsers, cosmic.getMenfessData());
        
    //     String output = outputStream.toString();
    //     assertTrue(output.contains("Mengurutkan dan mengirimkan fess"));
    //     assertTrue(output.contains("Test message"));
    // }

    @Test
    void testPilihanMenu_CosmicUser_ViewMutuals() {
        User[] testUsers = createTestUsers();
        Cosmic cosmic = (Cosmic) testUsers[1];
        tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
        String input = "3\n6\n"; // View mutuals then logout
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        tp4.pilihanMenu(cosmic, testUsers, cosmic.getMenfessData());
        
        String output = outputStream.toString();
        assertTrue(output.contains("Daftar mutuals BurhanFess"));
    }

    @Test
    void testPilihanMenu_CosmicUser_InvalidOption() {
        User[] testUsers = createTestUsers();
        Cosmic cosmic = (Cosmic) testUsers[1];
        tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
        String input = "99\n6\n"; // Invalid option then logout
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        tp4.pilihanMenu(cosmic, testUsers, cosmic.getMenfessData());
        
        String output = outputStream.toString();
        assertTrue(output.contains("Pilihan tidak valid"));
    }

    @Test
    void testStatusGame_InitialValue() {
        assertTrue(tp4.statusGame);
    }

    @Test
    void testMutualBook_Initialization() {
        User[] testUsers = createTestUsers();
        tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
        assertNotNull(tp4.mutualBook);
        assertEquals(testUsers.length, tp4.mutualBook.length);
        assertEquals(testUsers.length, tp4.mutualBook[0].length);
        
        // Check all values are initially 0
        for (int i = 0; i < testUsers.length; i++) {
            for (int j = 0; j < testUsers.length; j++) {
                assertEquals(0, tp4.mutualBook[i][j]);
            }
        }
    }

    @Test
    void testBurhanASCII_Display() {
        User[] testUsers = createTestUsers();
        Admin admin = (Admin) testUsers[0];
        tp4.mutualBook = new int[testUsers.length][testUsers.length];
        
        String input = "5\n"; // Logout
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        tp4.pilihanMenu(admin, testUsers, admin.getMenfessData());
        
        String output = outputStream.toString();
        assertTrue(output.contains("██████╗ ██╗   ██╗██████╗ ██╗  ██╗ █████╗ ███╗   ██║"));
        assertTrue(output.contains("███████╗███████╗███████╗███████╗"));
        assertTrue(output.contains("New Refactoring"));
        assertTrue(output.contains("inheritance &"));
        assertTrue(output.contains("Polymorphism"));
    }

    @Test
    void testDaftarUser_SingleUser() {
        String input = "1\ncosmic\nsingleUser\nsinglePass\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        User[] users = tp4.daftarUser();
        
        assertEquals(1, users.length);
        assertTrue(users[0] instanceof Cosmic);
        assertEquals("singleUser", users[0].getUsername());
        assertEquals("singlePass", users[0].getPassword());
        assertEquals(0, users[0].getId());
    }

    @Test
    void testLogin_CosmicUser() {
        User[] testUsers = createTestUsers();
        
        String input = "cosmicUser\ncosmicPass\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        tp4.input = new Scanner(System.in);
        
        User loggedInUser = tp4.login(testUsers);
        
        assertNotNull(loggedInUser);
        assertEquals("cosmicUser", loggedInUser.getUsername());
        assertTrue(loggedInUser instanceof Cosmic);
    }

    // Helper method to create test users
    private User[] createTestUsers() {
        User[] users = new User[2];
        users[0] = new Admin("adminUser", "adminPass", 0);
        users[1] = new Cosmic("cosmicUser", "cosmicPass", 1);
        return users;
    }
}