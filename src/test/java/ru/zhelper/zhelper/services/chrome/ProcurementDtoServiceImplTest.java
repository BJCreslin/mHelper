package ru.zhelper.zhelper.services.chrome;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.zhelper.zhelper.services.dao.ProcurementDataManager;

@ActiveProfiles("test")
class ProcurementDtoServiceImplTest {
    @MockBean
    ProcurementDataManager procurementDataManager;

    ProcurementDtoServiceImpl procurementDtoService = new ProcurementDtoServiceImpl(procurementDataManager);

    @Test
    void given615_remodelFzToInteger_thenInteger() {
        var Fz = "615";
        Assertions.assertEquals(615, procurementDtoService.remodelFzToInteger(Fz));
    }

    @Test
    void given44_remodelFzToInteger_thenInteger() {
        var Fz = "44-ФЗ";
        Assertions.assertEquals(44, procurementDtoService.remodelFzToInteger(Fz));
    }

    @Test
    void given223_remodelFzToInteger_thenInteger() {
        var Fz = "223";
        Assertions.assertEquals(223, procurementDtoService.remodelFzToInteger(Fz));
    }

    @Test
    void givenBad_remodelFzToInteger_thenZero() {
        var Fz = "99888544";
        Assertions.assertEquals(0, procurementDtoService.remodelFzToInteger(Fz));
    }

}