package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

import smartspace.data.ActionEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class MemoryUserDaoTests {

	public final String SMARTSPACE = "2019b.tomc";

	@Test
	public void testCreateUserEntityAndSeeIfItWasAdded() throws Exception {
		// GIVEN a dao is available
		MemoryUserDao dao = new MemoryUserDao();

		// WHEN we create a new user
		// AND we invoke create on the dao
		UserEntity userEntity = new UserEntity(SMARTSPACE, "Test@tesing.com", "Test", null, UserRole.PLAYER, 0);
		UserEntity rvUser = dao.create(userEntity);

		// THEN element was added to dao
		assertThat(dao.readAll()).containsExactly(rvUser);
	}

	@Test
	public void testCreateUserEntityAndSeeIfItHasAKey() throws Exception {
		// GIVEN a dao is available
		MemoryUserDao dao = new MemoryUserDao();

		// WHEN we create a new user
		// AND we invoke create on the dao
		UserEntity userEntity = new UserEntity(SMARTSPACE, "Test@tesing.com", "Test", null, UserRole.PLAYER, 0);
		UserEntity rvUser = dao.create(userEntity);

		// THEN element has a key
		assertThat(rvUser.getKey()).isNotNull().isNotEmpty();
	}

	@Test
	public void testDeleteAll() {
		// GIVEN a Dao is available
		// AND has some elements
		MemoryUserDao dao = new MemoryUserDao();
		UserEntity userEntity = new UserEntity(SMARTSPACE, "Test@tesing.com", "Test", null, UserRole.PLAYER, 0);
		UserEntity rvUser = dao.create(userEntity);
		UserEntity userSecondEntity = new UserEntity(SMARTSPACE, "Test2@tesing.com", "Test", null, UserRole.PLAYER, 0);
		UserEntity rvSecondUser = dao.create(userSecondEntity);
		
		// WHEN we delete all elements
		dao.deleteAll();

		// THEN all elements are deleted
		assertThat(dao.readAll()).isEmpty();
	}
	
	@Test
	public void testReadAllNotEmpty() {
		//GIVEN a Dao is available
		//AND has an element
		MemoryUserDao dao = new MemoryUserDao();
		UserEntity userEntity = new UserEntity(SMARTSPACE, "Test@tesing.com", "Test", null, UserRole.PLAYER, 0);
		UserEntity rvUser = dao.create(userEntity);
		
		// WHEN we read all elements
		dao.readAll();
		
		//THEN list is not empty
		assertThat(dao.readAll())
			.isNotNull()
			.isNotEmpty();
	}
}
