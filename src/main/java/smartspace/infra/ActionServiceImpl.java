package smartspace.infra;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartspace.aop.AdminUserGetActions;
import smartspace.aop.AdminUserPostActions;
import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ActionEntity;
import smartspace.layout.ActionBoundary;

@Service
public class ActionServiceImpl implements ActionService {
	private String smartspaceName;
	private EnhancedActionDao actionDao;
	private EnhancedElementDao<String> elementDao;

	@Autowired
	public ActionServiceImpl(EnhancedActionDao actionDao,EnhancedElementDao<String> elementDao) {
		this.actionDao = actionDao;
		this.elementDao = elementDao;
	}
	
	@Value("${smartspace.name}")
	public void setSmartspaceName(String smartspaceName) {
		this.smartspaceName = smartspaceName;
	}

	@Transactional
	@Override
	@AdminUserPostActions
	public List<ActionEntity> newAction(ActionBoundary[] allBoundaries, String adminSmartspace, String adminEmail) {
		List<ActionEntity> actionEntites = new LinkedList<>();

		for (ActionBoundary actionBoundary : allBoundaries) {
			ActionEntity actionEntity = actionBoundary.convertToEntity();
			elementDao.readById(actionEntity.getElementSmartspace() + "=" + actionEntity.getElementId())
					.orElseThrow(() -> new RuntimeException("Element doesn't exist"));

			if (valiadate(actionEntity)) {
				actionEntity.setCreationTimestamp(new Date());

				if (actionEntity.getActionSmartspace().equals(smartspaceName))
					throw new RuntimeException("Illigal Import!");
				else {
					actionEntites.add(this.actionDao.importAction(actionEntity));
				}
			} else
				throw new RuntimeException("invalid action");
		}
		return actionEntites;
	}

	private boolean valiadate(ActionEntity entity) {
		return entity.getPlayerEmail() != null && !entity.getPlayerEmail().trim().isEmpty()
				&& entity.getPlayerSmartspace() != null && !entity.getPlayerSmartspace().trim().isEmpty()
				&& entity.getActionSmartspace() != null && !entity.getActionSmartspace().trim().isEmpty()
				&& entity.getActionId() != null && !entity.getActionId().trim().isEmpty()
				&& entity.getElementSmartspace() != null && !entity.getElementSmartspace().trim().isEmpty()
				&& entity.getElementId() != null && !entity.getElementId().trim().isEmpty()
				&& entity.getActionType() != null && !entity.getActionType().trim().isEmpty();
	}

	@Override
	@AdminUserGetActions
	public List<ActionEntity> getUsingPagination(
			int size, int page, String adminSmartspace, String adminEmail) {
		
		return this.actionDao.readAll("key", size, page);
	}
}
