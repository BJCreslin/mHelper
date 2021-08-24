package ru.zhelper.zhelper.services;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import org.junit.Assert;
import ru.zhelper.zhelper.models.Procurement;

@SpringBootTest
@Sql(value = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestPropertySource(locations="classpath:test.properties")
public class ProcurementDataManagerImplTest  {

	@Autowired
	private ProcurementDataManagerImpl procurementDataManager;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementDataManagerImplTest.class);
	
	private final static String MY_UIN = "202320000012100777";
	private final static int FZ_NUMBER_OF_PREV = 44;
	
	
	@Test
	@Transactional
	public void testDataManager() {
		
		// 1. Create test data and save it
		Procurement p = new Procurement();
		p.setContractPrice(BigDecimal.TEN);
		p.setUin("ABC124z34");
		
		Procurement saved = procurementDataManager.saveProcurement(p);
		long savedId = saved.getId();
		Assert.assertEquals(saved.getContractPrice(), p.getContractPrice());
		Assert.assertEquals(saved.getUin(), p.getUin());
		
		LOGGER.info("Saved procurement has natively generated identity: {}", saved.getId());
		
		// 2. Get previous procurement
		Long idOfPrevious = saved.getId() - 1;
		Procurement prev = procurementDataManager.loadProcurement(idOfPrevious);
		LOGGER.info("Previous procurement: {}", prev);
		Assert.assertEquals(FZ_NUMBER_OF_PREV, prev.getFzNumber());
		
		// 3. Update some data of previous procurement
		prev.setUin(MY_UIN);
		Procurement updated = procurementDataManager.saveProcurement(prev);
		
		Assert.assertEquals(MY_UIN, updated.getUin());
		Assert.assertEquals(FZ_NUMBER_OF_PREV, updated.getFzNumber());
		
		// 4. Test getProcurementsByFzNumber()
		Assert.assertEquals(1, 
				procurementDataManager.getProcurementsByFzNumber(FZ_NUMBER_OF_PREV).size());
		
		// 5. Test deletion of object (not by id)
		procurementDataManager.deleteProcurement(prev);
		
		// 6. After deletion test getProcurementsByFzNumber() again.
		// Search by previous ID should not return a result since object was deleted.
		Assert.assertEquals(0,
				procurementDataManager.getProcurementsByFzNumber(FZ_NUMBER_OF_PREV).size());
		
		// 7. Delete other procurement by id
		procurementDataManager.deleteProcurementById(savedId);
	}
}
