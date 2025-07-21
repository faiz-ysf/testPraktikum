import java.time.LocalDateTime;


public class PromosiFess extends Menfess {
	public PromosiFess(User user, LocalDateTime waktu, String isiMenfess){
		super(user, waktu, isiMenfess);
	}

	@Override
	public String displayFess() {
    	return isiMenfess; 
	}
	
	@Override
	public String getTipeFess() {
		return "promotion";
	}
}