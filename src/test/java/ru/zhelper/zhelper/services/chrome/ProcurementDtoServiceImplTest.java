package ru.zhelper.zhelper.services.chrome;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.zhelper.zhelper.models.ProcedureType;
import ru.zhelper.zhelper.services.dao.ProcurementDataManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @NullAndEmptySource
    void givenNull_remodelProcedureType_thenDefaultValue(String procedureType) {
        Assertions.assertEquals(ProcedureType.DEFAULT_NONAME_PROCEDURE, procurementDtoService.remodelProcedureType(procedureType));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Bad1, Аукцион плохой"})
    void givenProcedure_remodelProcedureType_thenProcedureType(String procedureTypeString) {
        Assertions.assertEquals(ProcedureType.DEFAULT_NONAME_PROCEDURE, procurementDtoService.remodelProcedureType(procedureTypeString));
    }

    @ParameterizedTest
    @MethodSource("provideProcedureType")
    void givenNoPresentProcedure_remodelProcedureType_thenDefaultValue(String procedureTypeString, ProcedureType procedureType) {
        Assertions.assertEquals(procedureType, procurementDtoService.remodelProcedureType(procedureTypeString));
    }

    private static Stream<Arguments> provideProcedureType() {
        return Stream.of(
                Arguments.of("Электронный аукцион", ProcedureType.ELECTRONIC_AUCTION),
                Arguments.of("Закупка у единственного поставщика (подрядчика, исполнителя)", ProcedureType.PURCHASE_FROM_A_SINGLE_SUPPLIER),
                Arguments.of("Запрос котировок в электронной форме", ProcedureType.REQUEST_FOR_QUOTATIONS_IN_ELECTRONIC_FORM),
                Arguments.of("Способ определения поставщика (подрядчика, исполнителя), " +
                                "установленный Правительством Российской Федерации в соответствии со ст. 111 Федерального закона № 44-ФЗ",
                        ProcedureType.METHOD_FOR_DETERMINING_THE_SUPPLIER_BY_ARTICLE_111_44_FZ),
                Arguments.of("Открытый конкурс в электронной форме", ProcedureType.OPEN_ELECTRONIC_COMPETITION),
                Arguments.of("Закрытый аукцион", ProcedureType.CLOSED_AUCTION),
                Arguments.of("Запрос предложений в электронной форме", ProcedureType.REQUEST_FOR_PROPOSALS_IN_ELECTRONIC_FORM),
                Arguments.of("Предварительный отбор", ProcedureType.PRELIMINARY_SELECTION),
                Arguments.of("Конкурс с ограниченным участием в электронной форме", ProcedureType.COMPETITION_WITH_LIMITED_PARTICIPATION_IN_ELECTRONIC_FORM),
                Arguments.of("Запрос котировок", ProcedureType.REQUEST_FOR_QUOTATIONS),
                Arguments.of("Открытый конкурс", ProcedureType.OPEN_COMPETITION),
                Arguments.of("Закрытый конкурс", ProcedureType.CLOSED_COMPETITION),
                Arguments.of("Двухэтапный конкурс в электронной форме", ProcedureType.TWO_STAGE_ELECTRONIC_COMPETITION),
                Arguments.of("Закрытый двухэтапный конкурс", ProcedureType.CLOSED_TWO_STAGE_COMPETITION),
                Arguments.of("Конкурс с ограниченным участием", ProcedureType.COMPETITION_WITH_LIMITED_PARTICIPATION),
                Arguments.of("Двухэтапный конкурс", ProcedureType.TWO_STAGE_COMPETITION),
                Arguments.of("Запрос котировок без размещения извещения", ProcedureType.REQUEST_FOR_QUOTATIONS_WITHOUT_PLACING_A_NOTICE),
                Arguments.of("Запрос предложений", ProcedureType.REQUEST_FOR_PROPOSALS),
                Arguments.of("Закупка товара у единственного поставщика на сумму," +
                        " предусмотренную частью 12 статьи 93 Закона № 44-ФЗ", ProcedureType.PURCHASE_OF_GOODS_FROM_A_SINGLE_SUPPLIER),
                Arguments.of("Электронный аукцион (ПП РФ 615)", ProcedureType.ELECTRONIC_AUCTION_615FZ),
                Arguments.of("Предварительный отбор (ПП РФ 615)", ProcedureType.PRELIMINARY_SELECTION_615FZ),
                Arguments.of("223-ФЗ Прочие", ProcedureType.OTHER_223)

        );
    }
}
