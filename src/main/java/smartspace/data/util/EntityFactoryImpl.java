package smartspace.data.util;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Component
public class EntityFactoryImpl implements EntityFactory{
	
	@Override
	public UserEntity createNewUser(
			String userEmail, 
			String userSmartspace, 
			String username, 
			String avatar,
			UserRole role, 
			long points) {
		
		return new UserEntity(
				userSmartspace, 
				userEmail, 
				username,
				avatar,
				role,
				points);
	}
	
	@Override
	public ElementEntity createNewElement(
			String name, 
			String type, 
			Location location, 
			Date creationTimeStamp,
			String creatorEmail, 
			String creatorSmartSpace, 
			boolean expired, 
			Map<String, Object> moreAttributes) {
	
		return new ElementEntity(
				name,
				type,
				location, 
				creationTimeStamp,
				creatorEmail,
				creatorSmartSpace,
				expired,
				moreAttributes);
	}

	@Override
	public ActionEntity createNewAction(
			String elementId, 
			String elementSmartSpace, 
			String actionType,
			Date creationTimeStamp, 
			String playerEmail, 
			String playerSmartSpace, 
			Map<String, Object> moreAttributes) {
		
		return new ActionEntity(
				elementId,
				elementSmartSpace,
				actionType,
				creationTimeStamp,
				playerEmail,
				playerSmartSpace,
				moreAttributes);
	}
}
