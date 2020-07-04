package smartspace.data.util;

import java.util.Date;
import java.util.Map;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public interface EntityFactory {
	/**
	 * create a new UserEntity object with the given parameters
	 * @param userEmail: user's email address
	 * @param userSmartspace: to which smartspace the user is related
	 * @param username: user name in the system
	 * @param avatar: what avatar is being used by the user
	 * @param role: what is the role of the user
	 * @param points: how many points the user has
	 * 
	 *  @return new UserEntity object according to the given data
	 * */
	public UserEntity createNewUser(
			String userEmail,
			String userSmartspace,
			String username,
			String avatar,
			UserRole role,
			long points);
	
	/**
	 * create a new ElementEntity object with the given parameters
	 * @param name: the name of the element
	 * @param type: the type of the element in the system
	 * @param userSmartspace: to which smartspace the user is related
	 * @param location: ???
	 * @param creationTimeStamp: a timestamp of when the element was created
	 * @param creatorEmail: the email of the user that created the element
	 * @param creatorSmartSpace: the smartspace name where the user is located
	 * @param expired: true if the element is irrelevant and false otherwise
	 * @param moreAttributes: map containing more attributes
	 * 
	 *  @return new UserEntity object according to the given data
	 * */
	public ElementEntity createNewElement(
			String name,
			String type,
			Location location,
			Date creationTimeStamp,
			String creatorEmail,
			String creatorSmartSpace,
			boolean expired,
			Map<String,Object> moreAttributes);
	
	/**
	 * create a new UserEntity object with the given parameters
	 * 
	 * @param elementId: the element id of the used element
	 * @param elementSmartSpace: the smartspace name where the element is located
	 * @param actionType: the type of the action performed
	 * @param creationTimeStamp: a timestamp of when the element was created
	 * @param playerEmail: the email of the user that created the element
	 * @param playerSmartSpace: the smartspace name where the user is located
	 * @param moreAttributes: map containing more attributes
	 * 
	 *  @return new ActionEntity object according to the given data
	 * */
	public ActionEntity createNewAction(
			String elementId,
			String elementSmartSpace,
			String actionType,
			Date creationTimeStamp,
			String playerEmail,
			String playerSmartSpace,
			Map<String,Object> moreAttributes);
}
