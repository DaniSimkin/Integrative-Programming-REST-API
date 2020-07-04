package smartspace.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Service
public class RESTUserServiceImpl implements RESTUserService {

	private EnhancedUserDao<String> userDao;
	private String smartspaceName;
	
	@Autowired
	public RESTUserServiceImpl(EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}

	@Value("${smartspace.name}")
	public void setRESTUserSmartspaceName(String smartspaceName) {
		this.smartspaceName = smartspaceName;
	}
	
	@Override
	@Transactional
	public void updateUser(UserEntity user, String userSmartspace, String userEmail) { //, UserRole role) {
		
		UserEntity userWillingUpdateAnotherUser = this.userDao.readById(userSmartspace +"="+userEmail)
				.orElseThrow(() -> new RuntimeException("user willing to update another user is NOT in DB"));
		
		//user.setPoints(0);
		this.userDao.update(user);
	}
		
		
//		if(user.getRole() == UserRole.PLAYER && role != UserRole.PLAYER) {
//			user.setPoints(0);
//		}
//		this.userDao.update(user);
	
	
	@Override
	public UserEntity loginUser(String userSmartspace, String userEmail) {
		return this.userDao.readById(userSmartspace + "=" + userEmail)
				.orElseThrow(() -> new RuntimeException("user willing to Login is NOT in DB"));
	}

	private boolean validate(UserEntity entity) {
		return (entity.getAvatar() != null
				&& !entity.getAvatar().trim().isEmpty()
				&& entity.getUserName() != null
				&& !entity.getUserName().trim().isEmpty() 
				&& entity.getRole() != null);
	}
	
	@Override
	public UserEntity createANewUser(UserEntity createNewUser) {
		if (validate(createNewUser)) {
			createNewUser.setUserSmartspace(smartspaceName);
			this.userDao.importUser(createNewUser);
		} else {
			throw new RuntimeException("Invalid User");
		}
	return createNewUser;
	}
}

