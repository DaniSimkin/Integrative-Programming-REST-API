package smartspace.infra;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.plugin.Plugin;

@Service
public class RESTActionServiceImpl implements RESTActionService {
	
	private EnhancedActionDao actionDao;
	private EnhancedUserDao<String> userDao;
	private EnhancedElementDao<String> elementDao;
	private String smartspaceName;
	private ApplicationContext ctx;
	
	@Autowired
	public RESTActionServiceImpl(
			EnhancedActionDao actionDao, 
			EnhancedUserDao<String> userDao, 
			EnhancedElementDao<String> elementDao,
			ApplicationContext ctx) {
		
		this.actionDao = actionDao;
		this.elementDao = elementDao;
		this.userDao = userDao;
		this.ctx = ctx;
	}

	@Value("${smartspace.name}")
	public void setRESTActionSmartspaceName(String smartspaceName) {
		this.smartspaceName = smartspaceName;
	}
	
	@Override
	public ActionEntity invokeAction(ActionEntity newAction) {
		
		if (!valiadate(newAction)) {
			throw new RuntimeException("can NOT invoke action!");
		}
		
		if(newAction.getCreationTimestamp() == null) {
			newAction.setCreationTimestamp(new Date());
		}
				
//		if (!newAction.getActionSmartspace().equals(this.smartspaceName)) {
//			throw new RuntimeException("Illigal Import!");
//		}
	
		try {
			String funcName = newAction.getActionType();
//"ReserveTableInDiningRoom" ------>>>>>>> smartspace.plugin.ReserveTableInDiningRoomPlugin
//"GetTotalCost" ------>>>>>>> smartspace.plugin.GetTotalCostPlugin
//"RoomServiceReservation" ------>>>>>>> smartspace.plugin.RoomServiceReservationPlugin
//"CheckIn" ------>>>>>>> smartspace.plugin.CheckInPlugin

			String className = 
					"smartspace.plugin." 
					+ funcName.toUpperCase().charAt(0) 
					+ funcName.substring(1, funcName.length())
					+ "Plugin";
			Class<?> theClass = Class.forName(className);
			Plugin plugin = (Plugin) this.ctx.getBean(theClass);
			
			newAction = plugin.process(newAction);
			this.actionDao.create(newAction);

			return newAction;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	} 

	private boolean valiadate(ActionEntity actionEntity) {
		
	//check if all fields of actionEntity are NOT empty except getCreationTimestamp
	//and the player and the element does exist in DB
		UserEntity tmpUser;
		ElementEntity tmpElement;

		if(actionEntity.getPlayerEmail() != null 
				&& !actionEntity.getPlayerEmail().trim().isEmpty() 
				&& actionEntity.getPlayerSmartspace() != null 
				&& !actionEntity.getPlayerSmartspace().trim().isEmpty()
				//&& actionEntity.getActionSmartspace() != null 
				//&& !actionEntity.getActionSmartspace().trim().isEmpty()
				//&& actionEntity.getActionId() != null 
				//&& !actionEntity.getActionId().trim().isEmpty()
				&& actionEntity.getElementSmartspace() != null 
				&& !actionEntity.getElementSmartspace().trim().isEmpty()
				&& actionEntity.getElementId() != null 
				&& !actionEntity.getElementId().trim().isEmpty()
				&& actionEntity.getActionType() != null 
				&& !actionEntity.getActionType().trim().isEmpty()) {
		
			tmpUser = this.userDao.readById(
					actionEntity.getPlayerSmartspace()+"="+actionEntity.getPlayerEmail())
					.orElseThrow(() -> new RuntimeException("user do NOT exist"));
			
			if(tmpUser.getRole() != UserRole.PLAYER) {
				throw new RuntimeException("you must be a PLAYER for doing Actions !");	
			}
		
			tmpElement = this.elementDao.readById(
					actionEntity.getElementSmartspace()+"="+actionEntity.getElementId())
					.orElseThrow(() -> new RuntimeException("element do NOT exist"));
						
			return true;
		}
		return false;
	}
}

	

