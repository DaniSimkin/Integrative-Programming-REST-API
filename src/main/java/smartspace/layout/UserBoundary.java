package smartspace.layout;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.layout.data.CreatorBoundary;

public class UserBoundary {

	private CreatorBoundary key;
	private String userName;
	private String avatar;
	private UserRole role;
	private long points;

	public UserBoundary() {
	}

	public UserBoundary(UserEntity entity) {

		if (key == null) {
			this.key = new CreatorBoundary();
		}
		if (entity.getKey() != null) {
			String[] args = entity.getKey().split("=");
			this.key.setSmartspace(args[0]);
			this.key.setEmail(args[1]);
		}
		this.userName = entity.getUserName();
		this.avatar = entity.getAvatar();
		this.role = entity.getRole();
		this.points = entity.getPoints();
	}

	public CreatorBoundary getKey() {
		return key;
	}

	public void setKey(CreatorBoundary key) {
		this.key = key;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public UserEntity convertToEntity() {

		UserEntity entity = new UserEntity();

		if(this.getKey() != null) {
			if (this.getKey().getEmail() != null && this.getKey().getSmartspace() != null) {
				entity.setKey(this.getKey().getSmartspace() + "=" + this.getKey().getEmail());
			}
		}
		

		entity.setAvatar(this.getAvatar());

		entity.setPoints(this.getPoints());

		entity.setRole(this.getRole());

		entity.setUserName(this.getUserName());

		return entity;
	}

}

