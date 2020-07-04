package smartspace.infra;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public interface RESTUserService {

	public void updateUser(UserEntity user, String userSmartspace, String userEmail); //,UserRole role);

	//TODO:FIXED HERE no need for user boundary
	public UserEntity loginUser(String userSmartspace, String userEmail);

	public UserEntity createANewUser(UserEntity createNewUser);
}
