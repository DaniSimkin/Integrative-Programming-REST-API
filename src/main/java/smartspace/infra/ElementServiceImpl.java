package smartspace.infra;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartspace.aop.AdminUserGetActions;
import smartspace.aop.AdminUserPostActions;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ElementEntity;

@Service
public class ElementServiceImpl implements ElementService {
	private String smartspaceName;
	private EnhancedElementDao<String> elementDao;

	@Autowired
	public ElementServiceImpl(EnhancedElementDao<String> elementDao) {
		this.elementDao = elementDao;
	}
	
	@Value("${smartspace.name}")
	public void setSmartspaceName(String smartspaceName) {
		this.smartspaceName = smartspaceName;
	}

	@Override
	@Transactional
	@AdminUserPostActions
	public List<ElementEntity> newElement(ElementEntity[] allBoundaries, String adminSmartspace, String adminEmail) {
		List<ElementEntity> elementEntites = new LinkedList<>();
		for (ElementEntity elementEntity : allBoundaries) {
			if (valiadate(elementEntity)) {
				if (elementEntity.getElementSmartspace().equals(smartspaceName))
					throw new RuntimeException("Illigal Import!");
				else {
					elementEntites.add(this.elementDao.importElement(elementEntity));
				}
			} else {
				throw new RuntimeException("invalid element");
			}
		}
		return elementEntites;
	}

	private boolean valiadate(ElementEntity entity) {
		return entity.getCreatorEmail() != null && !entity.getCreatorEmail().trim().isEmpty()
				&& !entity.getCreatorSmartspace().trim().isEmpty() && entity.getCreatorSmartspace() != null
				&& entity.getName() != null && !entity.getName().trim().isEmpty() && !entity.getType().trim().isEmpty()
				&& entity.getType() != null;
	}

	@Override
	@AdminUserGetActions
	public List<ElementEntity> getUsingPagination(int size, int page, String adminSmartspace, String adminEmail) {
		return this.elementDao.readAll("key", size, page);
	}
	
}
