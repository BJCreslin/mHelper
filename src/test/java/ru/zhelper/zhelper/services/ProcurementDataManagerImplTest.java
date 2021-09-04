package ru.zhelper.zhelper.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.repository.ProcurementRepo;
import ru.zhelper.zhelper.services.exceptions.DataManagerException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@ActiveProfiles("test")
@SpringBootTest
@Sql(scripts = {"/sql/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProcurementDataManagerImplTest {

    private ProcurementDataManagerImpl procurementDataManager;


    @Autowired
    private ProcurementRepo repository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementDataManagerImplTest.class);

    private final static String MY_UIN = "202320000012100777";
    private final static Long ID_NON_EXISTING_PROCUREMENT = 8765L;
    private final static int FZ_NUMBER_OF_SECOND_PROCUREMENT = 44;
    private final static int FZ_NUMBER_OF_SAVED_PROCUREMENT = 615;
    private final static String DISPLAY_SAVED_ID = "Saved procurement has natively generated id: {}";
    private final static String EXCEPTION_NOT_RECEIVED = "Exception not received: ";

    @BeforeAll
    void init() {
        procurementDataManager = new ProcurementDataManagerImpl();
        procurementDataManager.setRepository(repository);

    }

    /**
     * 1. Create test data and save it
     */
    @Test
    @Transactional
    void testSave() {
        Procurement procurement = new Procurement();
        procurement.setContractPrice(BigDecimal.TEN);
        procurement.setUin("ABC124z34");
        procurement.setFzNumber(FZ_NUMBER_OF_SAVED_PROCUREMENT);

        Procurement saved =
                procurementDataManager.
                        save(procurement);

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
    void testGetOfUpdate() {

        Page<Procurement> allProcurements = procurementDataManager.findAll();

        System.out.println(" >>>>>>>>>> ALL PROCUREMENTS START");
        allProcurements.stream().forEach(System.out::println);
        System.out.println(" >>><<<<<<< ALL PROCUREMENTS END");

        Procurement second = procurementDataManager.loadById(allProcurements.get().findAny().get().getId());
        Assertions.assertEquals(FZ_NUMBER_OF_SAVED_PROCUREMENT, second.getFzNumber());

        second.setUin(MY_UIN);
        Procurement updated = procurementDataManager.save(second);

        Assertions.assertEquals(MY_UIN, updated.getUin());
        Assertions.assertEquals(FZ_NUMBER_OF_SAVED_PROCUREMENT, updated.getFzNumber());
    }

    /**
     * 4. Test getProcurementsByFzNumber()
     */
    @Test
    @Transactional
    void testGetProcurementsByFzNumber() {
        Assertions.assertEquals(1,
                procurementDataManager.getListByFzNumber(FZ_NUMBER_OF_SECOND_PROCUREMENT).size());
    }

    /**
     * 5. Test deletion of object (not by id)
     * 6. After deletion test getProcurementsByFzNumber() again.
     * Search by previous ID should not return a result since object was deleted.
     */
    @Test
    @Transactional
    void testDeleteAndCountRemaining() {
        List<Procurement> foundList = procurementDataManager.getListByFzNumber(FZ_NUMBER_OF_SECOND_PROCUREMENT);
        Assertions.assertEquals(1, foundList.size());

        procurementDataManager.delete(foundList.get(0));
        Assertions.assertEquals(0,
                procurementDataManager.getListByFzNumber(FZ_NUMBER_OF_SECOND_PROCUREMENT).size());
    }

    /**
     * 7. Delete a procurement by id and try to load it (-> Exception)
     */
    @Test
    @Transactional
    void testDeleteById() {

        Page<Procurement> allProcurements = procurementDataManager.findAll();

        Assertions.assertEquals(2, allProcurements.stream().count());

        System.out.println(" >>>>>>>>>> ALL PROCUREMENTS START");
        allProcurements.stream().forEach(System.out::println);
        System.out.println(" >>><<<<<<< ALL PROCUREMENTS END");

        procurementDataManager.deleteById(allProcurements.get().findAny().get().getId());

        // Assertion - is it really deleted?
        Assertions.assertThrows(JpaObjectRetrievalFailureException.class,
                () -> procurementDataManager.loadById(allProcurements.get().findAny().get().getId()));
    }

    @Test
    @Transactional
    void testLoadProcurementWithIdNull() {
        try {
            procurementDataManager.loadById(null);
        } catch (DataManagerException dataMgrExc) {
            if (!DataManagerException.COULD_NOT_LOAD_PROCUREMENT_NULL_DATA.equals(dataMgrExc.getMessage())) {
                fail(EXCEPTION_NOT_RECEIVED + DataManagerException.COULD_NOT_LOAD_PROCUREMENT_NULL_DATA);
            }
        }
    }

    @Test
    @Transactional
    void testDeleteNonExisting() {
        try {
            procurementDataManager.deleteById(ID_NON_EXISTING_PROCUREMENT);
        } catch (DataManagerException dataMgrExc) {
            String expectedMessage = String.format(
                    DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION, ID_NON_EXISTING_PROCUREMENT);
            if (!expectedMessage.equals(dataMgrExc.getMessage())) {
                fail(EXCEPTION_NOT_RECEIVED + expectedMessage);
            }
        }
    }

    @Test
    @Transactional
    void testFindAll() {

        Page<Procurement> allProcurements = procurementDataManager.findAll();

        Assertions.assertEquals(2, allProcurements.stream().count());

        System.out.println(" >>>>>>>>>> ALL PROCUREMENTS START");
        allProcurements.stream().forEach(System.out::println);
        System.out.println(" >>><<<<<<< ALL PROCUREMENTS END");
    }
}
