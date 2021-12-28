package ru.zhelper.zhelper.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.zhelper.zhelper.models.dto.ProcurementDto;
import ru.zhelper.zhelper.repository.ProcurementRepo;
import ru.zhelper.zhelper.services.ProcurementService;
import ru.zhelper.zhelper.services.chrome.ProcurementDtoService;
import ru.zhelper.zhelper.services.chrome.ProcurementDtoServiceImpl;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ChromeExtensionController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ChromeExtensionController")
class ChromeExtensionControllerTest {
    private final String PATH = ChromeExtensionController.URL+"/";
    private final ObjectMapper mapper = new ObjectMapper();
    @MockBean
    ProcurementDtoService service;
    @MockBean
    ProcurementRepo repo;
    @Autowired
    ChromeExtensionController controller;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addNewOne_Post_ShouldReturn() throws Exception {
        ProcurementDto procurement = getOneProcurementDto();
        System.out.println(mapper.writeValueAsString(procurement));
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(procurement));
        this.mockMvc.perform(builder)
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void addNewOneWithBadFz_Post_ShouldReturnBadRequest(String badItem) throws Exception {
        ProcurementDto procurement = getOneProcurementDto();
        procurement.setFzNumber(badItem);
        System.out.println(mapper.writeValueAsString(procurement));
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(procurement));
        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void addNewOneWithBadUin_Post_ShouldReturnBadRequest(String badItem) throws Exception {
        ProcurementDto procurement = getOneProcurementDto();
        procurement.setUin(badItem);
        System.out.println(mapper.writeValueAsString(procurement));
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(procurement));
        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void addNewOneWithBadObjectOf_Post_ShouldReturnBadRequest(String badItem) throws Exception {
        ProcurementDto procurement = getOneProcurementDto();
        procurement.setObjectOf(badItem);
        System.out.println(mapper.writeValueAsString(procurement));
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(procurement));
        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void addNewOneWithBadPublisherName_Post_ShouldReturnBadRequest(String badItem) throws Exception {
        ProcurementDto procurement = getOneProcurementDto();
        procurement.setPublisherName(badItem);
        System.out.println(mapper.writeValueAsString(procurement));
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(procurement));
        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void addNewOneWithBadProcedureType_Post_ShouldReturnBadRequest(String badItem) throws Exception {
        ProcurementDto procurement = getOneProcurementDto();
        procurement.setPublisherName(badItem);
        System.out.println(mapper.writeValueAsString(procurement));
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(procurement));
        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
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
