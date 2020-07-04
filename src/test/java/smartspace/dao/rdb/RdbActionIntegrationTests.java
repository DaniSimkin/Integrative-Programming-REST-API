package smartspace.dao.rdb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
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

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.util.EntityFactoryImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class RdbActionIntegrationTests {
	
	public final String SMARTSPACE = "2019b.tomc";
	
	private ActionDao actionDao;
	private EntityFactoryImpl factory;

	@Autowired
	public void setEleDao(ActionDao eleDao) {
		this.actionDao = eleDao;
	}

	@Autowired
	public void setFactory(EntityFactoryImpl factory) {
		this.factory = factory;
	}

	@After
	public void teardown() {
		this.actionDao.deleteAll();
	}

	@Test
	public void testCreateTenActionsAndVerifyIdsAreDifferent() throws Exception {
		// GIVEN messages table is empty

		// WHEN Create 10 Actions in DB
		Set<String> result = IntStream.range(1, 11)
				.mapToObj(i -> this.factory.createNewAction("Test #" + i, SMARTSPACE, "TEST", new Date(),
						"Test@tesing.com", SMARTSPACE, null))
				.map(this.actionDao::create)
				.map(ActionEntity::getKey)
				.collect(Collectors.toSet());

		// THEN all Actions get different id's
		assertThat(result).hasSize(10);
	}

	@Test
	public void testGenerateActionIdsAreUnique() throws Exception {
		// GIVEN the database is clean

		// WHEN I create some Actions to the database
		int size = 10;
		Set<String> ids = IntStream.range(1, size + 1) // Stream Integer
				.mapToObj(i -> this.factory.createNewAction("Test #" + i, SMARTSPACE, "TEST", new Date(),
						"Test@tesing.com", SMARTSPACE, new HashMap<>())) // ActionEntity Stream
				.map(this.actionDao::create) // ActionEntity Stream
				.map(ActionEntity::getKey) // String Stream
				.collect(Collectors.toSet());

		// THEN no id is repeated
		assertThat(ids).hasSize(size);

		assertThat(this.actionDao.readAll()).hasSize(size);
	}
}
