package ru.mhelper.models.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProcurementDtoTest {
    private final Validator validator;

    ProcurementDtoTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void givenOneProcurementDto_whenValidate_thenOk() {
        ProcurementDto procurement = getOneProcurementDto();
        Set<ConstraintViolation<ProcurementDto>> violations = validator.validate(procurement);
        assertThat(violations.size()).isZero();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenOneProcurementDtoWithoutFZ_whenValidate_thenNoConstraintViolations(String variable) {
        ProcurementDto procurement = getOneProcurementDto();
        procurement.setFzNumber(variable);
        Set<ConstraintViolation<ProcurementDto>> violations = validator.validate(procurement);
        assertThat(violations.size()).isPositive();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenOneProcurementDtoWithoutUin_whenValidate_thenNoConstraintViolations(String variable) {
        ProcurementDto procurement = getOneProcurementDto();
        procurement.setUin(variable);
        Set<ConstraintViolation<ProcurementDto>> violations = validator.validate(procurement);
        assertThat(violations.size()).isPositive();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenOneProcurementDtoWithoutObjectOf_whenValidate_thenNoConstraintViolations(String variable) {
        ProcurementDto procurement = getOneProcurementDto();
        procurement.setObjectOf(variable);
        Set<ConstraintViolation<ProcurementDto>> violations = validator.validate(procurement);
        assertThat(violations.size()).isPositive();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenOneProcurementDtoWithoutPublisherName_whenValidate_thenNoConstraintViolations(String variable) {
        ProcurementDto procurement = getOneProcurementDto();
        procurement.setPublisherName(variable);
        Set<ConstraintViolation<ProcurementDto>> violations = validator.validate(procurement);
        assertThat(violations.size()).isPositive();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void givenOneProcurementDtoWithoutProcedureType_whenValidate_thenNoConstraintViolations(String variable) {
        ProcurementDto procurement = getOneProcurementDto();
        procurement.setProcedureType(variable);
        Set<ConstraintViolation<ProcurementDto>> violations = validator.validate(procurement);
        assertThat(violations.size()).isPositive();
    }

    private ProcurementDto getOneProcurementDto() {
        return ProcurementDto.builder()
                .applicationDeadline("22.10.2021")
                .applicationSecure("5255.34")
                .contractPrice("193160")
                .contractSecure("56688.09")
                .dateOfAuction("31.10.2021")
                .dateOfPlacement("14.10.2021")
                .etpName("АО «ЕЭТП»")
                .etpUrl("http://roseltorg.ru")
                .fzNumber("44-ФЗ")
                .lastUpdatedFromEIS("22.10.2021")
                .linkOnPlacement("https://zakupki.gov.ru/epz/order/notice/zk20/view/common-info.html?regNumber=0361200010221000001")
                .objectOf("поставка хозяйственных товаров (клейкая лента упаковочная)")
                .procedureType("Запрос котировок в электронной форме")
                .publisherName("ГОСУДАРСТВЕННОЕ БЮДЖЕТНОЕ УЧРЕЖДЕНИЕ КУЛЬТУРЫ \"ЛИТЕРАТУРНО-ХУДОЖЕСТВЕННЫЙ МУЗЕЙ КНИГИ А. П. ЧЕХОВА \"ОСТРОВ САХАЛИН\"")
                .restrictions("1\nЗакупка у субъектов малого предпринимательства и социально ориентированных некоммерческих организаций в соответствии с ч. 3 ст. 30 Закона № 44-ФЗ\n2\nЗапрет на допуск товаров, работ, услуг при осуществлении закупок, а также ограничения и условия допуска в соответствии с требованиями, установленными ст. 14 Закона № 44-ФЗ\nДополнительные требования")
                .stage("Работа комиссии")
                .summingUpDate("31.10.2021")
                .timeOfAuction("09:35")
                .timeZone("Srednekolymsk Time МСК+8 (UTC+11)")
                .uin("0361200010221000001")
                .build();
    }
}