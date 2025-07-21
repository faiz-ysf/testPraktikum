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