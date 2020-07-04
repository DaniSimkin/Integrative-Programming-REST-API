package smartspace.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="USERS")
public class UserEntity implements SmartspaceEntity<String> {
	private String userSmartspace;
	private String userEmail;
	private String userName;
	private String avatar;
	private UserRole role;
	private long points;
	
	public UserEntity() {
	}

	public UserEntity(String userSmartspace, String userEmail, String userName, String avatar, UserRole role,
			long points) {
		this.userSmartspace = userSmartspace;
		this.userEmail = userEmail;
		this.userName = userName;
		this.avatar = avatar;
		this.role = role;
		this.points = points;
	}

	@Transient
	public String getUserSmartspace() {
		return userSmartspace;
	}

	@Transient
	public String getUserEmail() {
		return userEmail;
	}

	public String getUserName() {
		return userName;
	}

	public String getAvatar() {
		return avatar;
	}

	@Enumerated(EnumType.STRING)
	public UserRole getRole() {
		return role;
	}

	public long getPoints() {
		return points;
	}

	public void setUserSmartspace(String userSmartspace) {
		this.userSmartspace = userSmartspace;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	@Override
	@Id
	@Column(name="ID")
	public String getKey() {
		return this.userSmartspace + "=" + this.userEmail;
	}

	@Override
	public void setKey(String key) {
		String[] parts = key.split("=");
		this.userSmartspace = parts[0];
		this.userEmail = parts[1];
	}

}
