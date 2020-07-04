package smartspace.infra;

import java.util.List;

import smartspace.data.UserEntity;
import smartspace.layout.UserBoundary;


public interface UserService {
	
	public List<UserEntity> newUser(UserEntity[] allEntities, String adminSmartspace, String adminEmail);
	
	public List<UserEntity> getUsingPagination (int size, int page, String adminSmartspace, String adminEmail);
	
}
