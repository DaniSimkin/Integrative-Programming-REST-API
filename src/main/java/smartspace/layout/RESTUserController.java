package smartspace.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import smartspace.infra.RESTUserService;
import smartspace.layout.data.NewUserForm;

@RestController
public class RESTUserController {
	
	private RESTUserService restUserService;

	@Autowired
	public RESTUserController(RESTUserService restUserService) {
		this.restUserService = restUserService;
	}

	@RequestMapping(path = "/smartspace/users", 
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createANewUser(
			@RequestBody NewUserForm newUserForm) {
		return new UserBoundary(
				this.restUserService.createANewUser(newUserForm.convertToEntity()));
	}
	
	@RequestMapping(
			path = "/smartspace/users/login/{userSmartspace}/{userEmail}",
			method = RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public UserBoundary loginUser(
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail) {
		return new UserBoundary(
				this.restUserService.loginUser(userSmartspace, userEmail));
	}
	
	@RequestMapping(
			path="/smartspace/users/login/{userSmartspace}/{userEmail}",
			method=RequestMethod.PUT,
			//consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void updateUser (
			@RequestBody UserBoundary user,
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail) {		
			this.restUserService.updateUser(user.convertToEntity(), userSmartspace, userEmail); //, null);	
			}

}

