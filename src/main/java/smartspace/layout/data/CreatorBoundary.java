package smartspace.layout.data;

public class CreatorBoundary {
	
	private String email;
	private String smartspace;
	
	public CreatorBoundary() {
	}
	
	public CreatorBoundary(String smartspace, String email) {
		this.email = email;
		this.smartspace = smartspace;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSmartspace() {
		return smartspace;
	}

	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
}

/*package smartspace.layout.data;

public class UserKeyBoundary {

	private String email;
	private String smartspace;

	public UserKeyBoundary() {
	}

	public UserKeyBoundary(String smartspace, String email) {
		this.email = email;
		this.smartspace = smartspace;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSmartspace() {
		return smartspace;
	}

	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
}*/
