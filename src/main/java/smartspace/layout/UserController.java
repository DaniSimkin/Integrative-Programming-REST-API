package smartspace.layout;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.UserEntity;
import smartspace.infra.UserService;

@RestController
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(path = "/smartspace/admin/users/{adminSmartspace}/{adminEmail}", 
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] newUser(
			@RequestBody UserBoundary[] users,
			@PathVariable("adminSmartspace") String adminSmartspace, 
			@PathVariable("adminEmail") String adminEmail) {
		UserEntity[] entityArr = 
				Stream.of(users)
				.map(UserBoundary::convertToEntity)
				.collect(Collectors.toList())
				.toArray(new UserEntity[0]);
		return this.userService
				.newUser(entityArr, adminSmartspace, adminEmail)
				.stream()
				.map(UserBoundary::new)
				.collect(Collectors.toList())
				.toArray(new UserBoundary[0]);
	}

	@RequestMapping(path = "/smartspace/admin/users/{adminSmartspace}/{adminEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getUsingPagination(
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@PathVariable("adminSmartspace") String adminSmartspace, @PathVariable("adminEmail") String adminEmail)

	{
		return this.userService.getUsingPagination(size, page, adminSmartspace, adminEmail).stream()
				.map(UserBoundary::new).collect(Collectors.toList()).toArray(new UserBoundary[0]);
	}

}
