import java.time.LocalDateTime;

public class CurhatFess extends Menfess{
	
	
	public CurhatFess(User user, LocalDateTime waktu, String isiMenfess){
		super(user, waktu, isiMenfess);
	}
	
	@Override
	public String displayFess() {
    	return isiMenfess; 
	}
	@Override
	public String getTipeFess() {
		return "curhat";
	}
}