package bean;
import model.Sys;

public class User {
	private String username;
	private String password;
	private String address;
	private int id;
	private Cart cart;
	
	public User(String u_name, String pwrd, String address) throws Exception {
		for (String s: Sys.getInstance().getUsernames()) {
			if (s.equals(u_name)) {
				throw new Exception("username already taken.");
			}
		}
		if (!verifyPw(pwrd))
			throw new Exception("password does not fit requirements.");
		else {
			username = u_name;
			password = pwrd;
			cart = new Cart();
			id = Sys.getCounter();
			Sys.upCounter();
		}
	}
	
	public User(String u_name, String pwrd, int i) throws Exception {
		if (!verifyPw(pwrd))
			throw new Exception("password does not fit requirements.");
		else {
			username = u_name;
			password = pwrd;
			id = i;
		}
	}
	private boolean verifyPw(String pwd) {
		if (pwd.length() > 15 || pwd.length() < 5)
			return false;
		return true;
	}
	
	public int getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Cart getCart() {
		return cart;
	}
	
	@Override
	public String toString() {
		return username + " " + password;
	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	public String getAddress() {
		// TODO Auto-generated method stub
		return address;
	}
}
