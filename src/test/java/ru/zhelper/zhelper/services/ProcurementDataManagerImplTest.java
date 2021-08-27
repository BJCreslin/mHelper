package ru.zhelper.zhelper.services;

import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.List;

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
import ru.zhelper.zhelper.services.exceptions.DataManagerException;

@SpringBootTest
@Sql(value = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestPropertySource(locations="classpath:test.properties")
class ProcurementDataManagerImplTest  {

	@Autowired
	private ProcurementDataManagerImpl procurementDataManager;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementDataManagerImplTest.class);
	
	private final static String MY_UIN = "202320000012100777";
	private final static Long ID_OF_FIRST_PROCUREMENT = 1L;
	private final static Long ID_OF_SECOND_PROCUREMENT = 2L;
	private final static Long ID_NON_EXISTING_PROCUREMENT = 8765L;
	private final static int FZ_NUMBER_OF_SECOND_PROCUREMENT = 44;
	private final static int FZ_NUMBER_OF_SAVED_PROCUREMENT = 615;
	
	private final static String DISPLAY_SAVED_ID = "Saved procurement has natively generated id: {}";
	private final static String EXCEPTION_NOT_RECEIVED = "Exception not received: ";
	
	/**
	 * 1. Create test data and save it
	 */
	@Test
	@Transactional
	void testSaveEntity() {
		Procurement procurement = new Procurement();
		procurement.setContractPrice(BigDecimal.TEN);
		procurement.setUin("ABC124z34");
		procurement.setFzNumber(FZ_NUMBER_OF_SAVED_PROCUREMENT);
		
		Procurement saved = procurementDataManager.saveEntity(procurement);
		
		// Check the saved data
		Assertions.assertEquals(saved.getContractPrice(), procurement.getContractPrice());
		Assertions.assertEquals(saved.getUin(), procurement.getUin());
		Assertions.assertEquals(saved.getFzNumber(), procurement.getFzNumber());
		
		LOGGER.info(DISPLAY_SAVED_ID, saved.getId());
	}
	
	/**
	 * 2. Get procurement with ID 2
	 * 3. Update some data of second procurement
	 */
	@Test
	@Transactional
	void testGetOfEntityUpdate() {
		Procurement second = procurementDataManager.loadEntity(ID_OF_SECOND_PROCUREMENT);
		Assertions.assertEquals(FZ_NUMBER_OF_SECOND_PROCUREMENT, second.getFzNumber());		

		second.setUin(MY_UIN);
		Procurement updated = procurementDataManager.saveEntity(second);
		
		Assertions.assertEquals(MY_UIN, updated.getUin());
		Assertions.assertEquals(FZ_NUMBER_OF_SECOND_PROCUREMENT, updated.getFzNumber());
	}
	
	/**
	 * 4. Test getProcurementsByFzNumber()
	 */
	@Test
	@Transactional
	void testGetProcurementsByFzNumber() {
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
	void testDeleteAndCountRemaining() {
		List<Procurement> foundList = procurementDataManager.getProcurementsByFzNumber(FZ_NUMBER_OF_SECOND_PROCUREMENT);
		Assertions.assertEquals(1, foundList.size());

		procurementDataManager.deleteEntity(foundList.get(0));
		Assertions.assertEquals(0,
			procurementDataManager.getProcurementsByFzNumber(FZ_NUMBER_OF_SECOND_PROCUREMENT).size());
	}	
	
	/**
	 * 7. Delete a procurement by id and try to load it (-> Exception)
	 */
	@Test
	@Transactional
	void testDeleteEntityById() {
		procurementDataManager.deleteEntityById(ID_OF_FIRST_PROCUREMENT);
		
		// Assertion - is it really deleted?
		try {
			procurementDataManager.loadEntity(ID_OF_FIRST_PROCUREMENT);
		} catch (DataManagerException dataMgrExc) {
			String expectedMessage = String.format(
					DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION, ID_OF_FIRST_PROCUREMENT);
			if (!expectedMessage.equals(dataMgrExc.getMessage())) {
				fail(EXCEPTION_NOT_RECEIVED+expectedMessage);
			}
		}
	}
	
	@Test
	@Transactional
	void testLoadProcurementWithIdNull() {
		try {
			procurementDataManager.loadEntity(null);
		} catch (DataManagerException dataMgrExc) {
			if (!DataManagerException.COULD_NOT_LOAD_PROCUREMENT_NULL_DATA.equals(dataMgrExc.getMessage())) {
				fail(EXCEPTION_NOT_RECEIVED+DataManagerException.COULD_NOT_LOAD_PROCUREMENT_NULL_DATA);
			}
		}
	}

	@Test
	@Transactional
	void testDeleteNonExisting() {
		try {
			procurementDataManager.deleteEntityById(ID_NON_EXISTING_PROCUREMENT);
		} catch (DataManagerException dataMgrExc) {
			String expectedMessage = String.format(
					DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION, ID_NON_EXISTING_PROCUREMENT);
			if (!expectedMessage.equals(dataMgrExc.getMessage())) {
				fail(EXCEPTION_NOT_RECEIVED+expectedMessage);
			}
		}
	}
}
