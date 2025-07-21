import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class MenfessSubclassesTest {
    private User testUser;
    private User receiverUser;
    private LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        testUser = new Cosmic("testUser", "testPass", 1);
        receiverUser = new Cosmic("receiver", "receiverPass", 2);
        testTime = LocalDateTime.now();
    }

    @Test
    void testConfessFess() {
        ConfessFess confessFess = new ConfessFess(testUser, testTime, "Test confession", receiverUser);
        
        assertEquals("Test confession", confessFess.displayFess());
        assertEquals("confession", confessFess.getTipeFess());
        assertEquals(receiverUser, confessFess.getReceiver());
        assertFalse(confessFess.getIsHidden());
    }

    @Test
    void testCurhatFess() {
        CurhatFess curhatFess = new CurhatFess(testUser, testTime, "Test curhat");
        
        assertEquals("Test curhat", curhatFess.displayFess());
        assertEquals("curhat", curhatFess.getTipeFess());
        assertFalse(curhatFess.getIsHidden());
    }

    @Test
    void testPromosiFess() {
        PromosiFess promosiFess = new PromosiFess(testUser, testTime, "Test promotion");
        
        assertEquals("Test promotion", promosiFess.displayFess());
        assertEquals("promotion", promosiFess.getTipeFess());
        assertFalse(promosiFess.getIsHidden());
    }

    @Test
    void testMenfessHideUnhide() {
        Menfess menfess = new CurhatFess(testUser, testTime, "Test message");
        
        assertFalse(menfess.getIsHidden());
        
        menfess.hide();
        assertTrue(menfess.getIsHidden());
        
        menfess.unhide();
        assertFalse(menfess.getIsHidden());
    }
}