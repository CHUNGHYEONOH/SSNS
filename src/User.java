
public class User implements java.io.Serializable{

	private String id;
	private String password;
	private String email;
	private String name;
	private String age;
	private String image;
	
	public User() {
		
	}
	
	public User(String id, String password) {
		this.id = id;
		this.password = password;
	}
	
	public User(String id, String password, String email, String name, String age, String image) {
		this.id = id;
		this.password = password;
		this.email = email;
		this.name = name;
		this.age = age;
		this.image = image;
	}
	
	public String getID() {
		return id;
	}
	public String getPW() {
		return password;
	}
	public String getImage() {
		return image;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getAge() {
		return age;
	}
	
	
}
