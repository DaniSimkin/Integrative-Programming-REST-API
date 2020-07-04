package smartspace.infra;

import java.util.List;

import smartspace.data.ActionEntity;
import smartspace.layout.ActionBoundary;

public interface ActionService {

//	public ActionEntity newAction(
//			ActionEntity entity, 
//			String adminSmartspace, 
//			String adminEmail);

	public List<ActionEntity> getUsingPagination(int size, int page, String adminSmartspace, String adminEmail);

	List<ActionEntity> newAction(ActionBoundary[] allActions, String adminSmartspace, String adminEmail);

}
