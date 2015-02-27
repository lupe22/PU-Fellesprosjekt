package user;

/**
 * Represents a user in the system.
 * The class is immutable. That means no instance of this class can change any of its attributes.
 *
 */
public class User {
	private final String username;
	private final String name; 
	private final String password;
	private final String salt;
	private final String email;
	
	public User(String username, String name, String password, String salt, String email){
		this.username = username;
		this.name = name;
		this.password = password;
		this.salt = salt;
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public String getSalt() {
		return salt;
	}
	
	
	/**
	 * returns identical (but new) instance of this user (deep-copy).
	 */
	public User clone(){
		return new User(username, name, password, salt, email);
	}
	
//	public void receiveNotification(Notification n){
//		//....
//	}
}
