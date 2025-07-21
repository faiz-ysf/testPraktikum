import java.time.*;

public class Menfess {
	protected LocalDateTime waktu;
	protected String isiMenfess;
	private boolean isHidden;
	protected User user;
	
	public boolean getIsHidden() {
		return isHidden;
	}

	public Menfess(User user, LocalDateTime waktu, String isiMenfess){
		this.user = user;
		this.waktu = waktu;
		this.isiMenfess = isiMenfess;
	}
	
	public void hide() {
		this.isHidden = true;
	}
	public void unhide() {
		this.isHidden = false;
	}

	public String displayFess() {
    	return isiMenfess; 
	}
	
	public String getTipeFess() {
		return "menfess";
	}

}