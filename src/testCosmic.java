import org.junit.jupiter.api.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class testCosmic {
    private Cosmic cosmic;
    private User[] testUsers;
    private int[][] mutualBook;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private InputStream originalIn;

    @BeforeEach
    void setUp() {
        cosmic = new Cosmic("cosmicUser", "cosmicPass", 1);
        
        testUsers = new User[3];
        testUsers[0] = new Admin("admin1", "pass1", 0);
        testUsers[1] = new Cosmic("cosmic1", "pass2", 1);
        testUsers[2] = new Cosmic("cosmic2", "pass3", 2);
        
        mutualBook = new int[3][3];
        
        // Add test menfess
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plusMinutes(5);
        LocalDateTime past = now.minusMinutes(5);
        
        testUsers[1].addmenfess(new CurhatFess(testUsers[1], past, "Past curhat"));
        testUsers[1].addmenfess(new ConfessFess(testUsers[1], past, "Confession for cosmic2", testUsers[2]));
        testUsers[2].addmenfess(new PromosiFess(testUsers[2], past, "Promo message"));
        testUsers[2].addmenfess(new ConfessFess(testUsers[2], future, "Future confession", testUsers[1]));
        
        // Hide one menfess
        testUsers[2].getMenfessData()[0].hide();
        
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
        assertEquals("Cosmic", cosmic.getRole());
    }

    @Test
    void testChangePassword_Success() {
        String input = "newPassword123\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.changePassword(cosmic);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Password berhasil diubah"));
        assertEquals("newPassword123", cosmic.getPassword());
    }

    @Test
    void testChangePassword_SamePassword() {
        String input = "cosmicPass\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.changePassword(cosmic);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Password baru tidak boleh sama dengan password lama"));
    }

    @Test
    void testChangePassword_EmptyPassword() {
        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.changePassword(cosmic);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Password tidak boleh kosong"));
    }

    @Test
    void testLihatSemuaFessPublik_AllMenfess() {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.lihatSemuaFessPublik(testUsers, testUsers[1]);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Semua Menfess Publik yang Sudah Terkirim"));
        assertTrue(output.contains("Past curhat"));
        assertTrue(output.contains("Confession for cosmic2"));
        assertFalse(output.contains("Promo message")); // This should be hidden
    }

    @Test
    void testLihatSemuaFessPublik_ConfessionForUser() {
        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.lihatSemuaFessPublik(testUsers, testUsers[2]);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Semua Menfess Confession untuk Kamu"));
        assertTrue(output.contains("Confession for cosmic2"));
    }

    @Test
    void testLihatSemuaFessPublik_InvalidChoice() {
        String input = "3\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.lihatSemuaFessPublik(testUsers, testUsers[1]);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Pilihan tidak valid"));
    }

    @Test
    void testLihatSemuaFessPublik_NoVisibleMenfess() {
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
        
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.lihatSemuaFessPublik(testUsers, testUsers[1]);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Menfess masih sepi"));
    }

    @Test
    void testKirimFess_SingleCurhatFess() {
        String input = "1\n5\nTest message\ncurhat\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.kirimFess(testUsers[1], testUsers[1].getMenfessData(), testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Mengurutkan dan mengirimkan fess"));
        assertTrue(output.contains("Test message"));
        assertTrue(output.contains("curhat"));
    }

    @Test
    void testKirimFess_ConfessionFess() {
        String input = "1\n10\nConfession message\nconfession\ncosmic2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.kirimFess(testUsers[1], testUsers[1].getMenfessData(), testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Confession message"));
        assertTrue(output.contains("Kepada: cosmic2"));
    }

    @Test
    void testKirimFess_PromoFess() {
        String input = "1\n15\nPromo message\npromo\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.kirimFess(testUsers[1], testUsers[1].getMenfessData(), testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Promo message"));
        assertTrue(output.contains("promotion"));
    }

    @Test
    void testKirimFess_MultipleFess() {
        String input = "2\n5\nFirst message\ncurhat\n10\nSecond message\npromo\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.kirimFess(testUsers[1], testUsers[1].getMenfessData(), testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("First message"));
        assertTrue(output.contains("Second message"));
    }

    @Test
    void testKirimFess_InvalidQuantity() {
        String input = "0\n6\n1\n5\nTest message\ncurhat\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.kirimFess(testUsers[1], testUsers[1].getMenfessData(), testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Input harus antara 1 dan 5"));
    }

    @Test
    void testKirimFess_InvalidDelayInput() {
        String input = "1\nabc\n5\nTest message\ncurhat\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.kirimFess(testUsers[1], testUsers[1].getMenfessData(), testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Input salah! Masukkan angka"));
    }

    @Test
    void testKirimFess_InvalidReceiver() {
        String input = "1\n5\nConfession message\nconfession\ninvalidUser\ncosmic2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.kirimFess(testUsers[1], testUsers[1].getMenfessData(), testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Mohon tuliskan penerima dengan benar"));
    }

    @Test
    void testFollowUser_Success() {
        String input = "cosmic2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.followUser(testUsers[1], testUsers, mutualBook);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Berhasil mem-follow cosmic2"));
        assertEquals(1, mutualBook[1][2]);
    }

    @Test
    void testFollowUser_SelfFollow() {
        String input = "cosmic1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.followUser(testUsers[1], testUsers, mutualBook);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Tidak bisa mem-follow diri sendiri"));
    }

    @Test
    void testFollowUser_UserNotFound() {
        String input = "nonexistentUser\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.followUser(testUsers[1], testUsers, mutualBook);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Username tidak ditemukan"));
    }

    @Test
    void testFollowUser_AlreadyFollowing() {
        mutualBook[1][2] = 1; // Already following
        
        String input = "cosmic2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.followUser(testUsers[1], testUsers, mutualBook);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Anda sudah mem-follow cosmic2"));
    }

    @Test
    void testSeeMutuals() {
        mutualBook[1][2] = 1; // cosmic1 follows cosmic2
        mutualBook[2][1] = 1; // cosmic2 follows cosmic1
        
        cosmic.seeMutuals(testUsers[1], testUsers, mutualBook);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Daftar mutuals BurhanFess"));
        assertTrue(output.contains("cosmic1"));
        assertTrue(output.contains("cosmic2"));
    }

    @Test
    void testPrintMutuals_WithFollowers() {
        mutualBook[0][1] = 1; // admin1 follows cosmic1
        mutualBook[2][1] = 1; // cosmic2 follows cosmic1
        
        cosmic.printMutuals(1, testUsers, mutualBook);
        
        String output = outputStream.toString();
        assertTrue(output.contains("admin1"));
        assertTrue(output.contains("cosmic2"));
    }

    @Test
    void testPrintMutuals_NoFollowers() {
        cosmic.printMutuals(1, testUsers, mutualBook);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Pengguna belum di-follow siapapun"));
        assertTrue(output.contains("Pengguna belum mem-follow siapapun"));
    }

    @Test
    void testDelayKirim() {
        String result = cosmic.delayKirim(3600, 30, 15, 10, 20, 7, 2025);
        
        assertTrue(result.contains("Fess akan dikirimkan pada:"));
        assertTrue(result.contains("Juli"));
        assertTrue(result.contains("2025"));
    }

    @Test
    void testLihatSemuaFessPublik_UserWithNullMenfess() {
        User[] usersWithNull = new User[1];
        usersWithNull[0] = new Admin("testAdmin", "testPass", 0);
        
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.lihatSemuaFessPublik(usersWithNull, testUsers[1]);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Menfess masih sepi"));
    }

    @Test
    void testKirimFess_InvalidQuantityInput() {
        String input = "abc\n1\n5\nTest message\ncurhat\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        cosmic.input = new Scanner(System.in);
        
        cosmic.kirimFess(testUsers[1], testUsers[1].getMenfessData(), testUsers);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Input salah! Masukkan angka"));
    }
}