package ru.zhelper.zhelper.services.chrome;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.zhelper.zhelper.services.dao.ProcurementDataManager;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Test
    void given223_remodelDateLastUpdateToLocalDate_thenLocalDate() {
        var lastUpdateLocalDate = LocalDate.of(2021, 10, 31);
        var lastUpdate = "31.10.2021 (МСК+6)";
        Assertions.assertEquals(lastUpdateLocalDate, procurementDtoService.remodelDateLastUpdateToLocalDate(lastUpdate));
    }

    @Test
    void given44_remodelDateLastUpdateToLocalDate_thenLocalDate() {
        var lastUpdateLocalDate = LocalDate.of(2021, 11, 1);
        var lastUpdate = "01.11.2021";
        Assertions.assertEquals(lastUpdateLocalDate, procurementDtoService.remodelDateLastUpdateToLocalDate(lastUpdate));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenNull_remodelDateLastUpdateToLocalDate_thenNull(String nullDate) {
        Assertions.assertNull(procurementDtoService.remodelDateLastUpdateToLocalDate(nullDate));
    }

    @Test
    void given223_remodelDateOfPlacementToLocalDate_thenLocalDate() {
        var lastUpdateLocalDate = LocalDate.of(2021, 10, 31);
        var lastUpdate = "31.10.2021 (МСК+6)";
        Assertions.assertEquals(lastUpdateLocalDate, procurementDtoService.remodelDateOfPlacementToLocalDate(lastUpdate));
    }

    @Test
    void given44_remodelDateOfPlacementToLocalDate_thenLocalDate() {
        var lastUpdateLocalDate = LocalDate.of(2021, 11, 1);
        var lastUpdate = "01.11.2021";
        Assertions.assertEquals(lastUpdateLocalDate, procurementDtoService.remodelDateOfPlacementToLocalDate(lastUpdate));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenNull_remodelDateOfPlacementToLocalDate_thenNull(String nullDate) {
        Assertions.assertNull(procurementDtoService.remodelDateOfPlacementToLocalDate(nullDate));
    }

    @Test
    void given44_remodelPriceToBigDecimal_thenLocalDate() {
        var contractPrice = "123.42";
        var price = new BigDecimal(contractPrice);
        Assertions.assertEquals(price, procurementDtoService.remodelPriceToBigDecimal(contractPrice));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenNull_remodelPriceToBigDecimal_thenNull(String nullDate) {
        Assertions.assertNull(procurementDtoService.remodelPriceToBigDecimal(nullDate));
    }
}