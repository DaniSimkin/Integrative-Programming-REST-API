package smartspace.infra;

import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smartspace.aop.AdminUserGetActions;
import smartspace.aop.AdminUserPostActions;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;

@Service
public class UserServiceImpl implements UserService {

	private EnhancedUserDao<String> userDao;
	private String smartspaceName;

	@Autowired
	public UserServiceImpl(EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@Value("${smartspace.name}")
	public void setSmartspaceName(String smartspaceName) {
		this.smartspaceName = smartspaceName;
	}

	private boolean validate(UserEntity entity) {
		return entity.getAvatar() != null && !entity.getAvatar().trim().isEmpty() && entity.getUserName() != null
				&& !entity.getUserName().trim().isEmpty() && entity.getRole() != null;
	}

	@Override
	@Transactional
	@AdminUserPostActions
	public List<UserEntity> newUser(UserEntity[] allEntities, String adminSmartspace, String adminEmail) {
		
		List<UserEntity> userEntites = new LinkedList<>();
		for (UserEntity userEntity : allEntities) {
			if (validate(userEntity)) {
				if (userEntity.getUserSmartspace().equals(smartspaceName))
					throw new RuntimeException("Illigal Import!");
				else
					userEntites.add(this.userDao.importUser(userEntity));
			} else {
				throw new RuntimeException("Invalid User");
			}

		}
		return userEntites;
	}

	@Override
	@AdminUserGetActions
	public List<UserEntity> getUsingPagination(int size, int page, String adminSmartspace, String adminEmail) {
		return this.userDao.readAll("key", size, page);
	}
	
}
