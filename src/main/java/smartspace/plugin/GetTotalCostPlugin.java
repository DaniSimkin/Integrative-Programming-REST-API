package smartspace.plugin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;

@Component
public class GetTotalCostPlugin implements Plugin {
	
	private ObjectMapper jackson;
    private EnhancedElementDao<String> elementDao;
    private EnhancedActionDao actionDao;
    private int roomPrice;
    private int roomServicePrice;
    private int totalPrice;
	
	@Autowired
	public GetTotalCostPlugin(EnhancedElementDao<String> elementDao, EnhancedActionDao actionDao) {
		super();
		this.elementDao = elementDao;
		this.actionDao = actionDao;
		this.jackson = new ObjectMapper();
	}
	
	@Override
	public ActionEntity process(ActionEntity actionEntity) {
		
		try {		
			
			ElementEntity elementEntity = 
					 this.elementDao.readById(
							 actionEntity.getElementSmartspace() +"="+ actionEntity.getElementId())
					 .orElseThrow(() -> new RuntimeException("element does not exist"));
			
			//make sure the element is room	
			if(elementEntity.getType().toLowerCase().contains("%room%")) {
				throw new RuntimeException("I'm sorry but this is NOT a room!");
			}
						
			ElementInput roomPriceInput =
					this.jackson.readValue(
							this.jackson.writeValueAsString(
									elementEntity.getMoreAttributes()), ElementInput.class);
			
			roomPrice = roomPriceInput.getRoomPrice();

			List<ActionEntity> listActions = 
					this.actionDao.readAll()
					.stream()
					.filter(action -> 
					action.getElementId().equals(actionEntity.getElementId())
					&&action.getMoreAttributes().get("roomServicePrice") != null)
					.collect(Collectors.toList());
						
			roomServicePrice = 0;
			for (ActionEntity actionEntity2 : listActions) {
				roomServicePrice += (int)actionEntity2.getMoreAttributes().get("roomServicePrice");
			}
				
			totalPrice = 0;
			if(roomPrice >= 0) {
				totalPrice += roomPrice;
			}
			if(roomServicePrice >= 0) {
				totalPrice += roomServicePrice;
			}
			actionEntity.getMoreAttributes().put("TotalPrice", totalPrice);
			return actionEntity;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
