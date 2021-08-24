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

import ru.zhelper.zhelper.models.Procurement;

@SpringBootTest
@Sql(value = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestPropertySource(locations="classpath:test.properties")
public class ProcurementDataManagerImplTest  {

	@Autowired
	private ProcurementDataManagerImpl procurementDataManager;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementDataManagerImplTest.class);
	
	@Test
	@Transactional
	public void testSaveProcurement() {
		Procurement p = new Procurement();
		p.setId(123L);
		p.setContractPrice(BigDecimal.TEN);
		p.setUin("ABC124z34");
		
		LOGGER.info("Now we will save procurement...");
		procurementDataManager.saveProcurement(p);
	}
}
