package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import smartspace.data.ElementEntity;
//import java.util.List;
//import java.util.Map;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@TestPropertySource(properties= {"spring.profiles.active=default"})
public class MemoryElementDaoTests {
	
	@Test
	public void testCreateElementEntityAndSeeIfItWasAdded() throws Exception {
		// GIVEN a Dao is available
		MemoryElementDao dao = new MemoryElementDao();
		
		// WHEN we create a new element
		// AND we invoke create on the dao
		ElementEntity elementEntity = 
				new ElementEntity("2019b.tomc",null,null,"Test","Testing",new Date(),false,"2019b.tomc","Test@tesing.com",null);
		ElementEntity rvElement = dao.create(elementEntity);
		
		// THEN the element was added to the dao
		assertThat(dao.getElements()).isNotEmpty();
	}
	
	@Test
	public void testCreateElementEntityAndSeeIfItHasAKey() throws Exception {
		// GIVEN a Dao is available
		MemoryElementDao dao = new MemoryElementDao();
		
		// WHEN we create a new element
		// AND we invoke create on the dao
		ElementEntity elementEntity = 
				new ElementEntity("2019b.tomc",null,null,"Test","Testing",new Date(),false,"2019b.tomc","Test@tesing.com",null);
		ElementEntity rvElement = dao.create(elementEntity);
		
		// THEN the element has a key
		assertThat(rvElement.getKey())
			.isNotNull()
			.isNotEmpty();			
	}
	
	@Test
	public void testDeleteAll() throws Exception {
		//GIVEN a Dao is available
		//AND has some elements
		MemoryElementDao dao = new MemoryElementDao();
		ElementEntity elementEntity = 
				new ElementEntity("2019b.tomc",null,null,"Test","Testing",new Date(),false,"2019b.tomc","Test@tesing.com",null);
		ElementEntity rvElement = dao.create(elementEntity);
		elementEntity.setCreatorEmail("Tesxt@tesion.com");
		ElementEntity rvSecondElement = dao.create(elementEntity);
		
		// WHEN we delete all elements
		dao.deleteAll();
		
		//THEN all elements are deleted
		assertThat(dao.getElements())
			.isEmpty();
	}
	
	@Test(expected=Exception.class)
	public void testCreateWithNullElemenet() throws Exception{
		// GIVEN an empty dao
		MemoryElementDao dao = new MemoryElementDao();
		
		// WHEN I create null element
		dao.create(null);
		
		// THEN create method throws excetpion
	}
	
	@Test 
	public void testReadByIdWithValidId() throws Exception {
		
		// GIVEN a Dao is available
		// AND has some elements
		MemoryElementDao dao = new MemoryElementDao();
		ElementEntity elementEntity = 
				new ElementEntity("2019b.tomc",null,null,"Test","Testing",new Date(),false,"2019b.tomc","Test@tesing.com",null);
		ElementEntity rvElement = dao.create(elementEntity);
		
		// WHEN we search for element with valid ID
		String tmpElementKey = rvElement.getKey();
		
		ElementEntity elementFromDB = dao.readById(tmpElementKey).orElseThrow(() 
				-> new RuntimeException("could not find element by key")); 
		
		// THEN we get the element by key
		assertThat(elementFromDB)
			.isNotNull();
			/////////.extracting("name", "severity", "details")
			/////////.containsExactly(name, SeverityEnum.INFO, details);
		
		assertThat(elementFromDB.getKey()).isEqualTo(tmpElementKey);
	}
	
	
	@Test
	public void testReadAll() throws Exception{
		
		//GIVEN a Dao is available
		MemoryElementDao dao = new MemoryElementDao();

		//AND has some elements
		ElementEntity firstElementEntity = 
				new ElementEntity("2019b.tomc",null,null,"Test","Testing",new Date(),false,"2019b.tomc","Test@tesing1.com",null);
		ElementEntity rvFirstElement = dao.create(firstElementEntity);
		
		ElementEntity secondElementEntity = 
				new ElementEntity("2019b.tomc",null,null,"Test","Testing",new Date(),false,"2019b.tomc","Test@tesing2.com",null);
		ElementEntity rvSecondElement = dao.create(secondElementEntity);
		
		// WHEN we delete all elements
		List<ElementEntity> allElements = dao.readAll();
		
		//THEN all elements are deleted
		assertThat(allElements)
			.isNotEmpty();
	}
	
	@Test
	public void testDelete() throws Exception{
		
		//GIVEN a Dao is available
		MemoryElementDao dao = new MemoryElementDao();

		//AND has some elements
		ElementEntity firstElementEntity = 
				new ElementEntity("2019b.tomc",null,null,"Test","Testing",new Date(),false,"2019b.tomc","Test@tesing1.com",null);
		ElementEntity rvFirstElement = dao.create(firstElementEntity);
		
		ElementEntity secondElementEntity = 
				new ElementEntity("2019b.tomc",null,null,"Test","Testing",new Date(),false,"2019b.tomc","Test@tesing2.com",null);
		ElementEntity rvSecondElement = dao.create(secondElementEntity);
		
		// WHEN we delete all elements
		dao.delete(rvFirstElement);
		
		//THEN all elements are deleted
		assertThat(dao.getElements())
			.doesNotContain(rvFirstElement);
	}
	
	@Test
	public void testDeleteByKey() throws Exception{
		
		//GIVEN a Dao is available
		MemoryElementDao dao = new MemoryElementDao();
		
		//AND has some elements
		ElementEntity firstElementEntity = 
				new ElementEntity("2019b.tomc",null,null,"Test","Testing",new Date(),false,"2019b.tomc","Test@tesing1.com",null);
		ElementEntity rvFirstElement = dao.create(firstElementEntity);
		
		ElementEntity secondElementEntity = 
				new ElementEntity("2019b.tomc",null,null,"Test","Testing",new Date(),false,"2019b.tomc","Test@tesing2.com",null);
		ElementEntity rvSecondElement = dao.create(secondElementEntity);
		
		String key = rvFirstElement.getKey();
		
		// WHEN we delete all elements
		dao.deleteByKey(key);
		
		//THEN all elements are deleted
		assertThat(dao.getElements())
			.doesNotContain(rvFirstElement);
	}
	
	@Test
	public void testUpdate() throws Exception{
		
		//GIVEN a Dao is available
		MemoryElementDao dao = new MemoryElementDao();
		
		//AND has some elements
		
		String name1 = "Test1";
		String name2 = "Test2";
		
		ElementEntity firstElementEntity = 
				new ElementEntity("2019b.tomc",null,null,name1,"Testing",new Date(),false,"2019b.tomc","Test@tesing1.com",null);
		ElementEntity rvFirstElement = dao.create(firstElementEntity);
		
		ElementEntity secondElementEntity = 
				new ElementEntity("2019b.tomc",null,null,"Test","Testing",new Date(),false,"2019b.tomc","Test@tesing2.com",null);
		ElementEntity rvSecondElement = dao.create(secondElementEntity);
		
		assertThat(rvFirstElement.getName().equals(name1));
		firstElementEntity.setName(name2);
		
		// WHEN we delete all elements
		dao.update(firstElementEntity);
		
		//THEN all elements are deleted
		assertThat(rvFirstElement.getName().equals(name2));
		
	}
}
