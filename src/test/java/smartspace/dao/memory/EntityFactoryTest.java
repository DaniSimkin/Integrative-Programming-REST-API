package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Map;

import org.junit.Test;

import smartspace.data.ActionEntity;
import smartspace.data.util.EntityFactoryImpl;

public class EntityFactoryTest {

	@Test
	public void testCreateActionEntity() throws Exception {

		// GIVEN ActionEntity data
		String Smartspace = "SMARTSPACE-TEST";


		// WHEN we create a new entityFactory
		EntityFactoryImpl factory = new EntityFactoryImpl();

		// THEN the EntityFactory will creat new ActionEntity with the correct data
		factory.createNewAction(null,Smartspace,"Test",new Date(),"test@Testing",Smartspace,null);
		


	}

}
