package smartspace.infra;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartspace.aop.AdminUserGetActions;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Service
public class RESTElementServiceImpl implements RESTElementService {

	private EnhancedElementDao<String> elementDao;
	private EnhancedUserDao<String> userDao;
	
	@Autowired
	public void setElementDao(EnhancedElementDao<String> elementDao, EnhancedUserDao<String> userDao) {
		this.elementDao = elementDao;
		this.userDao = userDao;
	}

	@Override
	public void updateElement(ElementEntity elementEntity, String managerSmartspace, String managerEmail,
			String elementSmartspace, String elementId) {

		String key = managerSmartspace + "=" + managerEmail;
		UserEntity userEntity = this.userDao.readById(key)
				.orElseThrow(() -> new RuntimeException("user doesn't exist"));

		if (!userEntity.getRole().equals(UserRole.MANAGER)) {
			throw new RuntimeException("you are not MANAGER");
		}

		ElementEntity elementEntityToUpdate = this.elementDao.readById(elementSmartspace + "=" + elementId)
				.orElseThrow(() -> new RuntimeException("element to update is NOT in DB"));
		
		elementEntity.setKey(elementEntityToUpdate.getKey());
		
		elementDao.update(elementEntity);
	}

	private boolean validate(ElementEntity elementEntity) {
		return (
				elementEntity.getName() != null
				&& !elementEntity.getName().trim().isEmpty()
				&& elementEntity.getType() != null
				&& !elementEntity.getType().trim().isEmpty()
				&& elementEntity.getCreatorSmartspace() != null
				&& !elementEntity.getCreatorSmartspace().trim().isEmpty()
				&& elementEntity.getCreatorEmail() != null
				&& !elementEntity.getCreatorEmail().trim().isEmpty()
				&& elementEntity.getMoreAttributes() != null
				&& elementEntity.getLocation().getX() != 0
				&& elementEntity.getLocation().getY() != 0);
	}
	
	@Override
	public ElementEntity createNewElement(ElementEntity elementEntity, String smartspace, String email) {
		
		String key = smartspace + "=" + email;
		UserEntity userEntity = this.userDao.readById(key)
				.orElseThrow(() -> new RuntimeException("user doesn't exist"));

		if (!userEntity.getRole().equals(UserRole.MANAGER)) {
			throw new RuntimeException("you are not a MANAGER");
		}
		
		if (validate(elementEntity)) {
			if(elementEntity.getCreationTimestamp() == null) {
				elementEntity.setCreationTimestamp(new Date());
			}
			this.elementDao.create(elementEntity);
		} else {
			throw new RuntimeException("Invalid element");
		}
	return elementEntity;
	}
					
	@Override
	public ElementEntity findById(String userSmartspace, String userEmail, 
			String elementSmartspace, String elementId) {
		
		this.userDao.readById(userSmartspace + "=" + userEmail)
				.orElseThrow(() -> new RuntimeException("user doesn't exist"));
		
		return this.elementDao
				.readById(elementSmartspace + "=" + elementId)
				.orElseThrow(() -> new RuntimeException("Element not found!"));
	}
	
	@Override
	public List<ElementEntity> getUsingPagination(int size, int page, 
			String userSmartspace, String userEmail) {
		
		this.userDao.readById(userSmartspace + "=" + userEmail)
			.orElseThrow(() -> new RuntimeException("user doesn't exist"));

		return this.elementDao.readAll("key", size, page);
	}
	
	@Override
	public List<ElementEntity> findNearLocation(String userSmartspace, String userEmail,
			String search, double x, double y, double distance, int page, int size){
		
		this.userDao.readById(userSmartspace + "=" + userEmail)
			.orElseThrow(() -> new RuntimeException("user doesn't exist"));
		
		if(distance<0) {
			throw new RuntimeException("Value must be positive!");
		}
		
		return this.elementDao.readAllByDistanceFromLocation(
				new Location(x, y), distance, size, page);
	}
	
	@Override
	public Collection<ElementEntity> getElementsUsingPaginationOfName(
			String userSmartspace, String userEmail, 
			String name, int size, int page) {

		this.userDao.readById(userSmartspace + "=" + userEmail)
			.orElseThrow(() -> new RuntimeException("user doesn't exist"));

		return this.elementDao.readAllUsingName(name, size, page);
	}

	@Override
	public List<ElementEntity> getElementsUsingPaginationOfSpecifiedType(
			String userSmartspace, String userEmail,
			String type, int size, int page) {

		this.userDao.readById(userSmartspace + "=" + userEmail)
			.orElseThrow(() -> new RuntimeException("user doesn't exist"));

		return this.elementDao.readAllUsingType(type, size, page);

//		if (role == UserRole.MANAGER) {
//			return this.elementDao.readAllUsingType(type, size, page);
//		} else if (role == UserRole.PLAYER) {
//			return this.elementDao.readAllUsingTypeNotExpired(type, size, page);
//		} else {
//			throw new RuntimeException(
//					"The URl isn't match for manager or player. use another user or URL that match admin user");
//		}
	}
}
