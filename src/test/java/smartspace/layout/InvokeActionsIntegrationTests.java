package smartspace.layout;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.EnhancedActionDao;
import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactoryImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.profiles.acrive=default",/* "spring.jpa.hibernate.ddl-auto=create-drop" */})
public class InvokeActionsIntegrationTests {
private String smartspaceName; 
	
	private EnhancedElementDao<String> elementDao;
	private EnhancedUserDao<String> userDao;
	private EnhancedActionDao actionDao;

	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	
	private UserEntity userEntityAdmin;
	private UserEntity userEntityManager;
	private UserEntity userEntityPlayer;
	
	@Value("${smartspace.name}")
	public void setSmartspaceName(String smartspaceName) {
		this.smartspaceName = smartspaceName;
	}

	@Autowired
	public void setElementDao(EnhancedElementDao<String> elementDao) {
		this.elementDao = elementDao;
	}
	
	@Autowired
	public void setUserDao(EnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setActionDao(EnhancedActionDao actionDao) {
		this.actionDao = actionDao;
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}
		
	@PostConstruct
	public void init(){
		this.baseUrl = "http://localhost:" + port + "/smartspace/actions";
	}
	
	@Before
	public void before() {
		this.userEntityAdmin = new UserEntity(smartspaceName, "admin.creating.element@de.mo",
				"myAdminName", "myAvatar", UserRole.ADMIN, 1332);
		this.userEntityAdmin = this.userDao.create(userEntityAdmin);
		
		this.userEntityManager = new UserEntity(smartspaceName, "manager.creating.element@de.mo",
				"myManagerName", "myAvatar", UserRole.MANAGER, 13);
		this.userEntityManager = this.userDao.create(userEntityManager);
		
		this.userEntityPlayer = new UserEntity(smartspaceName, "player.creating.element@de.mo",
				"myPlayerName", "myAvatar", UserRole.PLAYER, 13333);
		this.userEntityPlayer = this.userDao.create(userEntityPlayer);
	}
	
	@After
	public void teardown() {
		this.elementDao.deleteAll();
		this.userDao.deleteAll();
		this.actionDao.deleteAll();
	}
	
	@Test(expected=Exception.class)
	public void testMannagerInvokeAction() throws Exception{
		//GIVEN I have an element in db on which I will invoke an action
		Location l = new Location(1,1);
		ElementEntity rv = this.elementDao.create(
				new EntityFactoryImpl()
				.createNewElement("bla", "Room", l, null, "bla@abc", smartspaceName, false, null));
		
		//WHEN I try to invoke an action using manager account
		ActionEntity createdAction = new ActionEntity();
		String[] splitedKey = rv.getKey().split("=");
		Map<String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("roomNumber",l);
		
		createdAction.setActionType("getTotalCost");
		createdAction.setElementId(splitedKey[1]);
		createdAction.setElementSmartspace(smartspaceName);
		createdAction.setMoreAttributes(moreAttributes);
		createdAction.setPlayerEmail(userEntityManager.getUserEmail());
		createdAction.setPlayerSmartspace(this.userEntityManager.getUserSmartspace());
		
		createdAction = this.actionDao.create(createdAction);
		
		ActionBoundary ab = new ActionBoundary(createdAction);
		
		this.restTemplate
		.postForObject(this.baseUrl,ab ,ActionBoundary.class);
		//THEN I get an exception for that action
	}
	
	@Test(expected=Exception.class)
	public void testAdminInvokeAction() throws Exception{
		//GIVEN I have an element in db on which I will invoke an action
		Location l = new Location(1,1);
		ElementEntity rv = this.elementDao.create(
				new EntityFactoryImpl()
				.createNewElement("bla", "Room", l, null, "bla@abc", smartspaceName, false, null));
		
		//WHEN I try to invoke an action using manager account
		ActionEntity createdAction = new ActionEntity();
		String[] splitedKey = rv.getKey().split("=");
		Map<String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("roomNumber",l);
		
		createdAction.setActionType("getTotalCost");
		createdAction.setElementId(splitedKey[1]);
		createdAction.setElementSmartspace(smartspaceName);
		createdAction.setMoreAttributes(moreAttributes);
		createdAction.setPlayerEmail(userEntityAdmin.getUserEmail());
		createdAction.setPlayerSmartspace(this.userEntityAdmin.getUserSmartspace());
		
		createdAction = this.actionDao.create(createdAction);
		
		ActionBoundary ab = new ActionBoundary(createdAction);
		
		this.restTemplate
		.postForObject(this.baseUrl,ab ,ActionBoundary.class);
		//THEN I get an exception for that action
	}
	
	@Test
	public void testPlayerInvokeActionGetTotalCost() throws Exception{
		//GIVEN I have an element in db on which I will invoke an action
		Location l = new Location(1,1);
		Map<String, Object> eleMoreAttributes = new HashMap<>();
		eleMoreAttributes.put("roomPrice", 5000);
		eleMoreAttributes.put("roomServicePrice", 200);
		ElementEntity rv = this.elementDao.create(
				new ElementEntity("test", "Room", l, null,
						"Sal@sad", smartspaceName, false, eleMoreAttributes));
		
		//WHEN I try to invoke an action using manager account
		ActionEntity createdAction = new ActionEntity();
		String[] splitedKey = rv.getKey().split("=");
		Map<String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("roomNumber",l);
		
		createdAction.setActionType("getTotalCost");
		createdAction.setElementId(splitedKey[1]);
		createdAction.setElementSmartspace(splitedKey[0]);
		createdAction.setMoreAttributes(moreAttributes);
		createdAction.setPlayerEmail(userEntityPlayer.getUserEmail());
		createdAction.setPlayerSmartspace(this.userEntityPlayer.getUserSmartspace());
		createdAction = this.actionDao.create(createdAction);
		
		ActionBoundary ab = new ActionBoundary(createdAction);
		
		ActionBoundary response= this.restTemplate
			.postForObject(this.baseUrl,ab ,ActionBoundary.class);
		
		//THEN
		Map<String, Object> neededProps = new HashMap<>();
		neededProps.put("TotalPrice", 5200);
		neededProps.put("roomNumber",l);
		
		assertThat(response)
			.isNotNull()
			.extracting("properties")
			.contains(neededProps);
	}
	
}
