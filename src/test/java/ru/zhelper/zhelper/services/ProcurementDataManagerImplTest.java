package ru.zhelper.zhelper.services;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.zhelper.zhelper.models.Procurement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-integrationTest-context-component-scan.xml"})
@Sql({"/test-schema.sql", "/test-data.sql"})
public class ProcurementDataManagerImplTest implements ApplicationContextAware {

	/**
	 * https://docs.spring.io/spring-framework/docs/4.2.0.RC2/spring-framework-reference/html/integration-testing.html
	 */
	private ApplicationContext applicationContext;
	
	@Autowired
	private ProcurementDataManager procurementDataManager;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementDataManagerImplTest.class);
	
	@Test
	@Transactional
	public void testSaveProcurement() {
		Procurement p = new Procurement();
		p.setId(123L);
		p.setContractPrice(BigDecimal.TEN);
		
		LOGGER.info("Now we will save procurement...");
		procurementDataManager.saveProcurement(p);
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.setApplicationContext(applicationContext);
	}
}
