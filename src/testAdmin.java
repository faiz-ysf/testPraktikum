import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class testAdmin {
    private Admin admin;
    private User[] testUsers;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private InputStream originalIn;

    @BeforeEach
    void setUp() {
        admin = new Admin("adminUser", "adminPass", 0);
        
        // Create test users with menfess data
        testUsers = new User[3];
        testUsers[0] = new Admin("admin1", "pass1", 0);
        testUsers[1] = new Cosmic("cosmic1", "pass2", 1);
        testUsers[2] = new Cosmic("cosmic2", "pass3", 2);
        
        // Add some test menfess
        LocalDateTime now = LocalDateTime.now();
        testUsers[1].addmenfess(new CurhatFess(testUsers[1], now, "Test curhat"));
        testUsers[1].addmenfess(new ConfessFess(testUsers[1], now, "Test confession", testUsers[2]));
        testUsers[2].addmenfess(new PromosiFess(testUsers[2], now, "Test promo"));
        
        // Hide one menfess for testing
        testUsers[1].getMenfessData()[0].hide();
        
        // Capture output
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
    void testGetRole() {
        assertEquals("admin", admin.getRole());
    }

    @Test
    void testDaftarPengguna() {
        admin.daftarPengguna(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("admin1"));
        assertTrue(output.contains("cosmic1"));
        assertTrue(output.contains("cosmic2"));
        assertTrue(output.contains("admin"));
        assertTrue(output.contains("Cosmic"));
    }

    @Test
    void testTampilkanKembaliFess_WithHiddenFess() {
        String input = "cosmic1\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        admin.input = new Scanner(System.in);
        
        admin.tampilkanKembaliFess(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Semua Menfess yang Disembunyikan"));
        assertTrue(output.contains("Fess berhasil dikembalikan"));
    }

    @Test
    void testTampilkanKembaliFess_NoHiddenFess() {
        // Unhide all menfess first
        for (User user : testUsers) {
            if (user.getMenfessData() != null) {
                for (Menfess menfess : user.getMenfessData()) {
                    if (menfess != null) {
                        menfess.unhide();
                    }
                }
            }
        }
        
        admin.tampilkanKembaliFess(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Tidak ada menfess yang disembunyikan"));
    }

    @Test
    void testTampilkanKembaliFess_InvalidUsername() {
        String input = "invalidUser\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        admin.input = new Scanner(System.in);
        
        admin.tampilkanKembaliFess(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Username tidak ditemukan"));
    }

    @Test
    void testTampilkanKembaliFess_InvalidIndex() {
        String input = "cosmic1\n999\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        admin.input = new Scanner(System.in);
        
        admin.tampilkanKembaliFess(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Index fess tidak valid"));
    }

    @Test
    void testTampilkanKembaliFess_InvalidInputType() {
        String input = "cosmic1\nabc\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        admin.input = new Scanner(System.in);
        
        admin.tampilkanKembaliFess(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Input harus berupa angka"));
    }

    @Test
    void testTampilkanKembaliFess_NullMenfess() {
        String input = "cosmic1\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        admin.input = new Scanner(System.in);
        
        admin.tampilkanKembaliFess(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Tidak ada menfess pada index tersebut"));
    }

    @Test
    void testTampilkanKembaliFess_AlreadyVisible() {
        // Make sure menfess at index 1 is visible
        testUsers[1].getMenfessData()[1].unhide();
        
        String input = "cosmic1\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        admin.input = new Scanner(System.in);
        
        admin.tampilkanKembaliFess(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Menfess pada index tersebut tidak disembunyikan"));
    }

    @Test
    void testSembunyikanKembaliFess_WithVisibleFess() {
        // Ensure menfess is visible
        testUsers[1].getMenfessData()[1].unhide();
        
        String input = "cosmic1\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        admin.input = new Scanner(System.in);
        
        admin.sembunyikankanKembaliFess(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Fess berhasil disembunyikan"));
    }

    @Test
    void testSembunyikanKembaliFess_NoVisibleFess() {
        // Hide all menfess
        for (User user : testUsers) {
            if (user.getMenfessData() != null) {
                for (Menfess menfess : user.getMenfessData()) {
                    if (menfess != null) {
                        menfess.hide();
                    }
                }
            }
        }
        
        admin.sembunyikankanKembaliFess(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Tidak ada menfess yang terlihat"));
    }

    @Test
    void testSembunyikanKembaliFess_AlreadyHidden() {
        String input = "cosmic1\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        admin.input = new Scanner(System.in);
        
        admin.sembunyikankanKembaliFess(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Menfess pada index tersebut sudah disembunyikan"));
    }

    @Test
    void testResetPassword_Success() {
        String input = "cosmic1\nnewPassword123\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        admin.input = new Scanner(System.in);
        
        admin.resetPassword(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Password berhasil diubah"));
        assertEquals("newPassword123", testUsers[1].getPassword());
    }

    @Test
    void testResetPassword_InvalidUsername() {
        String input = "invalidUser\nvalidUser\ncosmic1\nnewPassword\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        admin.input = new Scanner(System.in);
        
        admin.resetPassword(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("username yang anda masukkan salah"));
    }

    @Test
    void testResetPassword_EmptyPassword() {
        String input = "cosmic1\n\nvalidPassword\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        admin.input = new Scanner(System.in);
        
        admin.resetPassword(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Password tidak boleh kosong"));
    }

    @Test
    void testResetPassword_SamePassword() {
        String input = "cosmic1\npass2\nnewPassword\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        admin.input = new Scanner(System.in);
        
        admin.resetPassword(testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Password baru tidak boleh sama dengan password lama"));
    }

    @Test
    void testTampilkanKembaliFess_UserWithNoMenfessData() {
        // Create user with null menfess data
        User[] usersWithNull = new User[1];
        usersWithNull[0] = new Admin("testAdmin", "testPass", 0);
        
        String input = "testAdmin\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        admin.input = new Scanner(System.in);
        
        admin.tampilkanKembaliFess(usersWithNull);
        
        String output = outputStream.toString();
        assertTrue(output.contains("User tidak memiliki data menfess"));
    }
}