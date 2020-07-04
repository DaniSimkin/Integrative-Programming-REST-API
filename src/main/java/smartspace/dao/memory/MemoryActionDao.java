package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;

//@Repository
public class MemoryActionDao implements ActionDao {

	private List<ActionEntity> actionEntities; 
	private AtomicLong actionID;  
	
	public MemoryActionDao() {
		this.actionEntities = Collections.synchronizedList(new ArrayList<ActionEntity>());
		this.actionID = new AtomicLong(1);
	}
	
	@Override
	public ActionEntity create(ActionEntity actionEntity) {
		long tmpID = actionID.getAndIncrement(); 
		String tmpKey = actionEntity.getActionSmartspace() + "=" + tmpID;
		actionEntity.setKey(tmpKey);
		actionEntities.add(actionEntity);
		return actionEntity;
	}

	@Override
	public List<ActionEntity> readAll() {
		return this.actionEntities;
	}

	@Override
	public void deleteAll() {
		this.actionEntities.clear();;
	}

	protected List<ActionEntity> getElements() {
		return this.actionEntities;
	}

}
