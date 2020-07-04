package smartspace.dao.rdb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.util.EntityFactoryImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class RdbElementIntegrationTests {
	public final String SMARTSPACE = "2019b.tomc";
	
	private ElementDao<String> eleDao;
	private EntityFactoryImpl factory;

	@Autowired
	public void setEleDao(ElementDao<String> eleDao) {
		this.eleDao = eleDao;
	}
	
	@Autowired
	public void setFactory(EntityFactoryImpl factory) {
		this.factory = factory;
	}

	@After
	public void teardown() {
		this.eleDao.deleteAll();
	}

	@Test
	public void testCreateTenElementsAndFindById() throws Exception {
		// GIVEN messages table is empty

		// WHEN Create 10 element in DB
		Set<String> result = IntStream.range(1, 11)
				.mapToObj(i -> this.factory.createNewElement("Test #" + i,
						"tests", new Location(), new Date(),
						"Test@testing.com",
						SMARTSPACE, false, null))
				.map(this.eleDao::create)
				.map(ElementEntity::getKey)
				.collect(Collectors.toSet());

		// THEN all elements get different id's
		assertThat(result)
			.hasSize(10);
	}
	
	@Test
	public void testCreateUpdateAndRead() throws Exception{
		// GIVEN Elements table is empty
		
		// WHEN Create in DB new Element
		// AND Update element details
		// AND Read element from database
		ElementEntity newElement = 
				this.eleDao.create(this.factory.createNewElement("Test",
				"tests", new Location(), new Date(),
				"Test@testing.com",
				SMARTSPACE, false, null));
		
		Map<String, Object> updatedDetails = new TreeMap<>();
		updatedDetails.put("x", "y");
		updatedDetails.put("y", 8);
		ElementEntity update = new ElementEntity();
		update.setKey(newElement.getKey());
		update.setMoreAttributes(updatedDetails);
		
		update.setName("updated test");
		
		this.eleDao.update(update);
		
		Optional<ElementEntity> rv = this.eleDao.readById(newElement.getKey());
		
		ObjectMapper jackson = new ObjectMapper();
		Map<String, Object>jacksonDetail = 
			jackson.readValue(	
				jackson.writeValueAsString(updatedDetails),
				Map.class);
		
		// THEN the message exists
		// AND the message name is "Test"
		// AND the details are updated
		assertThat(rv)
			.isPresent()
			.get()
			.extracting("name", "moreAttributes")
			.containsExactly("updated test", jacksonDetail);
	}
	
	@Test
	public void testGenerateElementIdsAreUnique() throws Exception{
		//GIVEN the database is clean 
		
		// WHEN I create some elements to the database
		int size = 10;
		Set<String> ids = 
		IntStream.range(1, size+1) // Stream Integer
			.mapToObj(i->this.factory.createNewElement("Test",
					"tests", new Location(), new Date(),
					"Test@testing.com",
					SMARTSPACE, false,
					new HashMap<>())) // ElementEntity Stream
			.map(this.eleDao::create) // ElementEntity Stream
			.map(ElementEntity::getKey) // String Stream
			.collect(Collectors.toSet());
		
		// THEN no id is repeated
		assertThat(ids)
			.hasSize(size);
		
		assertThat(this.eleDao.readAll())
			.hasSize(size);
	}
}
