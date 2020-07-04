package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import smartspace.dao.UserDao;
import smartspace.data.UserEntity;

//@Repository
public class MemoryUserDao implements UserDao<String> {

	private List<UserEntity> userEntities; 
	private AtomicLong userEntityID;
	
	public MemoryUserDao() {
		this.userEntities = Collections.synchronizedList(new ArrayList<UserEntity>());
		this.userEntityID = new AtomicLong(1);
	}
	
	@Override
	public UserEntity create(UserEntity userEntity) {
		long tmpID = userEntityID.getAndIncrement(); 
		String tmpKey = userEntity.getUserSmartspace() + "=" + tmpID;  
		userEntity.setKey(tmpKey);
		userEntities.add(userEntity);
		return userEntity;
	}

	@Override
	public Optional<UserEntity> readById(String userKey) {
		UserEntity tmpUserEntity = null;
		for (UserEntity userEntity : userEntities) {
			if(userEntity.getKey().equals(userKey)) {
				tmpUserEntity = userEntity;
			}
		}
		if(tmpUserEntity != null) {
			return Optional.of(tmpUserEntity);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public void update(UserEntity userEntity) {
		synchronized (this.userEntities) {
			UserEntity tmpUser = readById(userEntity.getKey()).orElseThrow(() -> 
				new RuntimeException("user couldn't be found"));
			
			if(userEntity.getPoints() != tmpUser.getPoints()) {
				tmpUser.setPoints(userEntity.getPoints());
			}
			if(userEntity.getAvatar() != null) {
				tmpUser.setAvatar(userEntity.getAvatar());
			}
			if(userEntity.getRole() != null) {
				tmpUser.setRole(userEntity.getRole());
			}
			if(userEntity.getUserEmail() != null) {
				tmpUser.setUserEmail(userEntity.getUserEmail());
			}
			if(userEntity.getUserName() != null) {
				tmpUser.setUserName(userEntity.getUserName());
			}
			if(userEntity.getUserSmartspace() != null) {
				tmpUser.setUserSmartspace(userEntity.getUserSmartspace());
			}
		}
	}
	
	@Override
	public List<UserEntity> readAll() {
		return this.userEntities;
	}

	@Override
	public void deleteAll() {
		this.userEntities.clear();
	}

//	@Override
//	public List<UserEntity> readMessageWithNameContaining(String text, int size, int page) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
