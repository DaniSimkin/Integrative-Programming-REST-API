package smartspace.infra;

import smartspace.data.ActionEntity;

public interface RESTActionService {

	public ActionEntity invokeAction(ActionEntity newAction);

}
