package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Date;
import org.junit.Test;
import smartspace.data.ActionEntity;

public class MemoryActionDaoTests {

	public final String SMARTSPACE = "2019b.tomc";

	@Test
	public void testCreateActionEntityAndSeeIfItWasAdded() throws Exception {
		// GIVEN a Dao is available
		MemoryActionDao dao = new MemoryActionDao();

		// WHEN we create a new action
		// AND we invoke create on the dao
		ActionEntity actionEntity = new ActionEntity(SMARTSPACE, null, SMARTSPACE, null, SMARTSPACE, "Test@tesing.com",
				"Test", new Date(), null);
		ActionEntity rvAction = dao.create(actionEntity);

		// THEN the element was added to the dao
		assertThat(dao.getElements()).containsExactly(rvAction);

	}

	@Test
	public void testCreateActionEntityAndSeeIfItHasAKey() throws Exception {
		// GIVEN a Dao is available
		MemoryActionDao dao = new MemoryActionDao();

		// WHEN we create a new action
		// AND we invoke create on the dao
		ActionEntity actionEntity = new ActionEntity(SMARTSPACE, null, SMARTSPACE, null, SMARTSPACE, "Test@tesing.com",
				"Test", new Date(), null);
		ActionEntity rvAction = dao.create(actionEntity);

		// THEN the element has a key
		assertThat(rvAction.getKey()).isNotNull().isNotEmpty();

	}

	@Test
	public void testDeleteAll() throws Exception {
		// GIVEN a Dao is available
		// AND has some elements
		MemoryActionDao dao = new MemoryActionDao();
		ActionEntity actionEntity = new ActionEntity(SMARTSPACE, null, SMARTSPACE, null, SMARTSPACE, "Test@tesing.com",
				"Test", new Date(), null);
		ActionEntity rvAction = dao.create(actionEntity);
		actionEntity.setPlayerEmail("Tesxt@tesion.com");
		ActionEntity rvSecondAction = dao.create(actionEntity);

		// WHEN we delete all elements
		dao.deleteAll();

		// THEN all elements are deleted
		assertThat(dao.getElements()).isEmpty();
	}

	@Test
	public void testReadAllNotEmpty() {
		// GIVEN a Dao is available
		// AND has an element
		MemoryActionDao dao = new MemoryActionDao();
		ActionEntity actionEntity = new ActionEntity(SMARTSPACE, null, SMARTSPACE, null, SMARTSPACE, "Test@tesing.com",
				"Test", new Date(), null);
		ActionEntity rvAction = dao.create(actionEntity);

		// WHEN we read all elements
		dao.readAll();

		// THEN list is not empty
		assertThat(dao.getElements()).isNotNull().isNotEmpty();
	}
}
