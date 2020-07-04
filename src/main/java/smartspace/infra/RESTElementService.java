package smartspace.infra;

import java.util.Collection;
import java.util.List;

import smartspace.data.ElementEntity;
import smartspace.data.UserRole;

public interface RESTElementService {
	
	public ElementEntity createNewElement(ElementEntity elementEntity, String managerSmartspace,  
			String managerEmail);
	
	public void updateElement(ElementEntity elementEntity, String managerSmartspace,  
			String managerEmail, String elementSmartspace, String elementId);
	
	public ElementEntity findById(String userSmartspace, String userEmail, 
			String elementSmartspace, String elementId);
	
	public List<ElementEntity> findNearLocation(String userSmartspace,String userEmail,
			String search,double x,double y,double distance,int page,int size);
		
	public Collection<ElementEntity> getElementsUsingPaginationOfName(
			String userSmartspace, String userEmail, 
			String name, int size, int page);

	public List<ElementEntity> getElementsUsingPaginationOfSpecifiedType(
			String userSmartspace, String userEmail,
			String type, int size, int page);

	public List<ElementEntity> getUsingPagination(int size, int page, 
			String userSmartspace, String userEmail);
}

