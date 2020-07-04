package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import smartspace.infra.RESTActionServiceImpl;

@RestController
public class RESTActionController {

	private RESTActionServiceImpl restActionServiceImpl;

	@Autowired
	public RESTActionController(RESTActionServiceImpl restActionServiceImpl) {
		this.restActionServiceImpl = restActionServiceImpl;
	}

	@RequestMapping(path = "/smartspace/actions",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary invokeAction(
			@RequestBody ActionBoundary action) {
		return new ActionBoundary(
				this.restActionServiceImpl.invokeAction(action.convertToEntity()));
	}
	
}
