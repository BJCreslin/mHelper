package ru.zhelper.zhelper.services;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import ru.zhelper.zhelper.models.Procurement;

@SpringBootTest
@Sql(value = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestPropertySource(locations="classpath:test.properties")
public class ProcurementDataManagerImplTest  {

	@Autowired
	private ProcurementDataManagerImpl procurementDataManager;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementDataManagerImplTest.class);
	
	private final static String MY_UIN = "202320000012100777";
	private final static Long ID_OF_FIRST_PROCUREMENT = 1L;
	private final static Long ID_OF_SECOND_PROCUREMENT = 2L;
	private final static Long ID_OF_SAVED_PROCUREMENT = 3L;
	private final static int FZ_NUMBER_OF_SECOND_PROCUREMENT = 44;
	private final static int FZ_NUMBER_OF_SAVED_PROCUREMENT = 615;
	
	/**
	 * 1. Create test data and save it
	 */
	@Test
	@Transactional
	public void testSaveEntity() {
		Procurement procurement = new Procurement();
		procurement.setContractPrice(BigDecimal.TEN);
		procurement.setUin("ABC124z34");
		procurement.setFzNumber(FZ_NUMBER_OF_SAVED_PROCUREMENT);
		
		Procurement saved = procurementDataManager.saveEntity(procurement);
		Assertions.assertEquals(saved.getContractPrice(), procurement.getContractPrice());
		Assertions.assertEquals(saved.getUin(), procurement.getUin());
		
		LOGGER.info("Saved procurement has natively generated identity: {}", saved.getId());
	}
	
	/**
	 * 2. Get procurement with ID 2
	 * 3. Update some data of previous procurement
	 */
	@Test
	@Transactional
	public void testGetOfEntityUpdate() {
		Procurement prev = procurementDataManager.loadEntity(ID_OF_SECOND_PROCUREMENT);
		LOGGER.info("Previous procurement: {}", prev);
		Assertions.assertEquals(FZ_NUMBER_OF_SECOND_PROCUREMENT, prev.getFzNumber());		

		prev.setUin(MY_UIN);
		Procurement updated = procurementDataManager.saveEntity(prev);
		
		Assertions.assertEquals(MY_UIN, updated.getUin());
		Assertions.assertEquals(FZ_NUMBER_OF_SECOND_PROCUREMENT, updated.getFzNumber());
	}
	
	/**
	 * 4. Test getProcurementsByFzNumber()
	 */
	@Test
	@Transactional
	public void testGetProcurementsByFzNumber() {
		Assertions.assertEquals(1, 
				procurementDataManager.getProcurementsByFzNumber(FZ_NUMBER_OF_SECOND_PROCUREMENT).size());
	}	
	
	/**
	 * 5. Test deletion of object (not by id)
	 * 6. After deletion test getProcurementsByFzNumber() again.
	 * Search by previous ID should not return a result since object was deleted.
	 */
	@Test
	@Transactional
	public void testDeleteAndCountRemaining() {
		Procurement saved = procurementDataManager.loadEntity(ID_OF_SAVED_PROCUREMENT);
		Assertions.assertEquals(FZ_NUMBER_OF_SAVED_PROCUREMENT, saved.getFzNumber());

		procurementDataManager.deleteEntity(saved);
		Assertions.assertEquals(0,
			procurementDataManager.getProcurementsByFzNumber(FZ_NUMBER_OF_SECOND_PROCUREMENT).size());
	}	
	
	/**
	 * 7. Delete a procurement by id
	 */
	@Test
	@Transactional
	public void testDeleteEntityById() {
		procurementDataManager.deleteEntityById(ID_OF_FIRST_PROCUREMENT);
	}
}
