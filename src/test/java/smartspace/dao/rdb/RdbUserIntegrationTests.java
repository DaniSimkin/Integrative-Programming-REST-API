package smartspace.dao.rdb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


import smartspace.dao.UserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactoryImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class RdbUserIntegrationTests {
	public final String SMARTSPACE = "2019b.tomc";

	private UserDao<String> userDao;
	private EntityFactoryImpl factory;

	@Autowired
	public void setEleDao(UserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setFactory(EntityFactoryImpl factory) {
		this.factory = factory;
	}

	@After
	public void teardown() {
		this.userDao.deleteAll();
	}

	@Test
	public void testCreateTenUsersAndFindById() throws Exception {
		// GIVEN messages table is empty

		// WHEN Create 10 users in DB
		Set<String> result = IntStream.range(1, 11)
				.mapToObj(i -> this.factory.createNewUser("Test"+i+"@tesing.com",SMARTSPACE, "Test", 
						null, UserRole.PLAYER, 0))
				.map(this.userDao::create)
				.map(UserEntity::getKey)
				.collect(Collectors.toSet());

		// THEN all users get different id's
		assertThat(result)
			.hasSize(10);
	}
	
	
//	@Test
//	public void testGetAllUsersByPatternAndPagination() throws Exception{
//		// GIVEN the database contains 12 users with name containing 'abc'
//		// AND the database contains 20 users that do not have with name containing 'abc'
//		IntStream.range(0, 12) // Stream Integer
//		.mapToObj(i->this.factory.createNewUserEntity("Test@tesing.com","smartspace", "Test", 
//				null, UserRole.PLAYER, 0))
//				 // UserEntity Stream
//		.forEach(this.userDao::create);
//
//		IntStream.range(12, 33) // Stream Integer
//		.mapToObj(i->this.factory.createNewUserEntity(
//				"Test@tesing.com","smartspace", "Test", 
//				null, UserRole.PLAYER, 0)) // UserEntity Stream
//		.forEach(this.userDao::create);
//
//		// WHEN I read 10 messages with name containing 'abc' after skipping first 10 messages
//		List<UserEntity> result = this.userDao.readMessageWithNameContaining("abc", 10, 1);
//		
//		// THEN I receive 2 results
//		assertThat(result)
//			.hasSize(2);
//		
//	}
	
	
	@Test
	public void testGenerateUserIdsAreUnique() throws Exception{
		//GIVEN the database is clean 
		
		// WHEN I create some elements to the database
		int size = 10;
		Set<String> ids = 
		IntStream.range(1, size+1) // Stream Integer
			.mapToObj(i->this.factory.createNewUser("Test"+i+"@tesing.com",SMARTSPACE, "Test", 
					null, UserRole.PLAYER, 0)) // ElementEntity Stream
			.map(this.userDao::create) // ElementEntity Stream
			.map(UserEntity::getKey) // String Stream
			.collect(Collectors.toSet());
		
		// THEN no id is repeated
		assertThat(ids)
			.hasSize(size);
		
		assertThat(this.userDao.readAll())
			.hasSize(size);
	}
}
