import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class testUser {
    private User user;
    private Admin admin;
    private Cosmic cosmic;

    @BeforeEach
    void setUp() {
        user = new User("testUser", "testPass", 1);
        admin = new Admin("adminUser", "adminPass", 2);
        cosmic = new Cosmic("cosmicUser", "cosmicPass", 3);
    }

    @Test
    void testUserConstructorAndGetters() {
        assertEquals("testUser", user.getUsername());
        assertEquals("testPass", user.getPassword());
        assertEquals(1, user.getId());
        assertEquals("User", user.getRole());
        assertNotNull(user.getMenfessData());
    }

    @Test
    void testSetPassword() {
        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void testAddMenfess() {
        Menfess menfess = new CurhatFess(user, java.time.LocalDateTime.now(), "Test message");
        user.addmenfess(menfess);
        
        assertEquals(menfess, user.getMenfessData()[0]);
    }

    @Test
    void testAddMenfess_ArrayFull() {
        // Fill the array to capacity
        for (int i = 0; i < 100; i++) {
            user.addmenfess(new CurhatFess(user, java.time.LocalDateTime.now(), "Message " + i));
        }
        
        // Try to add one more - should not be added
        Menfess extraMenfess = new CurhatFess(user, java.time.LocalDateTime.now(), "Extra message");
        user.addmenfess(extraMenfess);
        
        // The 101st message should not be in the array
        assertNull(user.getMenfessData()[100]);
    }

    @Test
    void testAdminRole() {
        assertEquals("admin", admin.getRole());
    }

    @Test
    void testCosmicRole() {
        assertEquals("Cosmic", cosmic.getRole());
    }
}