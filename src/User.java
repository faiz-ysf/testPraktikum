public class User {
	private String username;
	private String password;
	private int userId;
	private Menfess[] menfess;
	private int iterations;

	
	public User(String user, String pass, int id) {
		this.username = user;
		this.password = pass;
		this.userId = id;
		this.menfess = new Menfess[100];
	}

	public String getUsername(){
		return username;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String pass){
		this.password = pass;
	}

	public int getId() {
		return userId;
	}

	public Menfess[] getMenfessData() {
		return menfess;
	}

	public String getRole(){
		return "User";
	}

	public void addmenfess(Menfess Menfess){
		if (iterations >= menfess.length) {
			System.out.println("Menfess sudah penuh, tidak bisa menambah menfess baru.");
		} else {
			menfess[iterations] = Menfess;
			iterations++;
		}
	}

}