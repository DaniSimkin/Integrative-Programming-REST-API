package smartspace.layout;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.EnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.infra.UserService;
import smartspace.infra.UserServiceImpl;
import smartspace.layout.data.CreatorBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class RESTUserIntegrationTests {
	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	private EnhancedUserDao<String> userDao;
	// TODO: create 2 users
	private UserEntity userAdminTest;
	private UserEntity userPlayerTest;

	@Autowired
	public void setElementDao(EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/users/{adminSmartspace}/{adminEmail}";
	}

	@Before
	public void before()
	{
		this.userAdminTest = new UserEntity("2019b.tomc", "admin.creating.action@de.mo", "myAdminName", "myAvatar",
				UserRole.ADMIN, 1332);
		this.userAdminTest = this.userDao.create(userAdminTest);

		this.userPlayerTest = new UserEntity("2019b.tomc", "manager.creating.action@de.mo", "myManagerName", "myAvatar",
				UserRole.PLAYER, 13);
		this.userPlayerTest = this.userDao.create(userPlayerTest);
	}
	
	@After
	public void teardown() {
		this.userDao.deleteAll();
	}

	@Test
	public void testPostNewUser() throws Exception {
		// GIVEN the database has an admin user and non admin user

		// WHEN I POST new user
		UserBoundary newUser = new UserBoundary(userAdminTest);
		CreatorBoundary ukb = new CreatorBoundary();
		ukb.setEmail("Or@Test");
		ukb.setSmartspace("DaniTest.smartspace");
		newUser.setKey(ukb);
		newUser.setRole(UserRole.ADMIN);
		newUser.setAvatar("TheAvatar");
		newUser.setPoints(100);
		newUser.setUserName("TestTest");

		UserBoundary[] arr = { newUser };

		this.restTemplate.postForObject(this.baseUrl, arr, UserBoundary[].class, userAdminTest.getUserSmartspace(),
				userAdminTest.getUserEmail());

		// THEN the database contains a single message
		assertThat(this.userDao.readAll()).hasSize(3);
	}

	@Test(expected = Exception.class)
	public void testPostNewUserWithBadCode() throws Exception {
		// GIVEN the database has an admin user and non admin user

		UserBoundary newUser = new UserBoundary(userPlayerTest);
		CreatorBoundary ukb = new CreatorBoundary();
		ukb.setEmail("Or@Test");
		ukb.setSmartspace("DaniTest.smartspace");
		newUser.setKey(ukb);
		newUser.setRole(UserRole.PLAYER);
		newUser.setAvatar("TheAvatar");
		newUser.setPoints(100);
		newUser.setUserName("TestTest");
		UserBoundary[] arr = { newUser };
		this.restTemplate.postForObject(this.baseUrl, arr, UserBoundary[].class, userPlayerTest.getUserSmartspace(),
				userPlayerTest.getUserEmail());

		// THEN the test end with exception

	}

}
