package smartspace.dao;

import java.util.List;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;

public interface ActionDao {
	public ActionEntity create(ActionEntity actionEntity);
	public List<ActionEntity> readAll();
	public void deleteAll();
}

