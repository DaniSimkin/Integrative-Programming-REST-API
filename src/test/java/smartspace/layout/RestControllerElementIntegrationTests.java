package smartspace.layout;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

import smartspace.dao.EnhancedElementDao;
import smartspace.dao.EnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.layout.data.Key;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.profiles.acrive=default"})
public class RestControllerElementIntegrationTests {
	private String smartspaceName; 
	
	private EnhancedElementDao<String> elementDao;
	private EnhancedUserDao<String> userDao;

	private String baseUrl;
	private int port;
	private RestTemplate restTemplate;
	
	private UserEntity userEntityAdmin;
	private UserEntity userEntityManager;
	
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
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
		this.restTemplate = new RestTemplate();
	}
		
	@PostConstruct
	public void init(){
		this.baseUrl = "http://localhost:" + port + "/smartspace/elements/{userSmartspace}/{userEmail}";
	}
	
	@Before
	public void before() {
		this.userEntityAdmin = new UserEntity(smartspaceName, "admin.creating.element@de.mo",
				"myAdminName", "myAvatar", UserRole.ADMIN, 1332);
		this.userEntityAdmin = this.userDao.create(userEntityAdmin);
		
		this.userEntityManager = new UserEntity(smartspaceName, "manager.creating.element@de.mo",
				"myManagerName", "myAvatar", UserRole.MANAGER, 13);
		this.userEntityManager = this.userDao.create(userEntityManager);
	}
	
	@After
	public void teardown() {
		this.elementDao.deleteAll();
		this.userDao.deleteAll();
	}
	
	@Test
	public void testAddThreeLocationsAndSearchByLocationWithResults() throws Exception{
		// GIVEN the database contains 3 messages
		
		Map<String, Object> elementProperties = new HashMap<>();
		elementProperties.put("key1", 1);
		elementProperties.put("key2", "2");
		elementProperties.put("key3", "it can be anything");
			
		Location location = new Location();
		location.setX(1);
		location.setY(1); 
			
		int size = 3;
		
		List<Key> allElements = 
				IntStream.range(1, size + 1)
				.mapToObj(i->new ElementEntity( 
						"demo" + i, "MyType", new Location(location.getX()+i,location.getY()+i), new Date(), 
						this.userEntityAdmin.getUserEmail(), 
						this.userEntityAdmin.getUserSmartspace(), 
						false, elementProperties))
				.map(this.elementDao::create)
				.map(ElementBoundary::new)
				.map(ElementBoundary::getKey)
				.collect(Collectors.toList());

		// WHEN I GET messages of size 10 and page 0
		ElementBoundary[] response = this.restTemplate
			.getForObject(this.baseUrl + 
				"?search={search}&distance={distance}&x={x}&y={y}&size={size}&page={page}", 
				ElementBoundary[].class, 
				this.userEntityAdmin.getUserSmartspace(), 
				this.userEntityAdmin.getUserEmail(), 
				"location",5,1,1,10, 0);
		
		// THEN I receive the exact 3 elements written to the database
		assertThat(response)
		.hasSize(3)
		.extracting("key")
		.usingElementComparatorOnFields("id")
		.containsExactlyElementsOf(allElements);
	}
	
	@Test(expected=Exception.class)
	public void testAddElementWithLocationAndSerachByLocationWithNegativeDistance() throws Exception{
		// GIVEN the database contains 3 messages
		
		Map<String, Object> elementProperties = new HashMap<>();
		elementProperties.put("key1", 1);
		elementProperties.put("key2", "it can be anything");
			
		Location location = new Location();
		location.setX(1);
		location.setY(1); 
			
		int size = 3;
		IntStream.range(1, size + 1)
				.mapToObj(i->new ElementEntity( 
						"demo" + i, "MyType", new Location(location.getX()+i,location.getY()+i), new Date(), 
						this.userEntityAdmin.getUserEmail(), 
						this.userEntityAdmin.getUserSmartspace(), 
						false, elementProperties))
				.map(this.elementDao::create)
				.collect(Collectors.toList());

		// WHEN I GET messages of size 10 and page 0
		this.restTemplate.getForObject(this.baseUrl + 
				"?search={search}&distance={distance}&x={x}&y={y}&size={size}&page={page}", 
				ElementBoundary[].class, 
				this.userEntityAdmin.getUserSmartspace(), 
				this.userEntityAdmin.getUserEmail(), 
				"location",-5,1,1,10, 0);
		
		// THEN I get an exception
	}
	
	@Test
	public void testAddElementWithLocationAndSerachByLocationUsingDefaults() throws Exception{
		// GIVEN the database contains 3 messages
		
		Map<String, Object> elementProperties = new HashMap<>();
		elementProperties.put("key1", 1);
		elementProperties.put("key2", "it can be anything");
			
		Location location = new Location();
		location.setX(1);
		location.setY(1); 
			
		int size = 3;
		List<Key> allElements = 
				IntStream.range(1, size + 1)
				.mapToObj(i->new ElementEntity( 
						"demo" + i, "MyType", new Location(location.getX()+i,location.getY()+i), new Date(), 
						this.userEntityAdmin.getUserEmail(), 
						this.userEntityAdmin.getUserSmartspace(), 
						false, elementProperties))
				.map(this.elementDao::create)
				.map(ElementBoundary::new)
				.map(ElementBoundary::getKey)
				.collect(Collectors.toList());

		// WHEN I GET messages of size 10 and page 0
		ElementBoundary[] response = this.restTemplate
				.getForObject(this.baseUrl + 
				"?search={search}", 
				ElementBoundary[].class, 
				this.userEntityAdmin.getUserSmartspace(), 
				this.userEntityAdmin.getUserEmail(), 
				"location");
		
		// THEN I receive the exactly 1 element written to the database
		assertThat(response)
		.hasSize(1)
		.extracting("key")
		.usingElementComparatorOnFields("id")
		.containsExactly(allElements.get(0));
	}
}
