package ru.thelper.services.chrome;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.thelper.exceptions.BadRequestException;
import ru.thelper.models.BaseStatus;
import ru.thelper.models.dto.ProcurementAddress;
import ru.thelper.models.dto.ProcurementDto;
import ru.thelper.models.procurements.ProcedureType;
import ru.thelper.models.procurements.Procurement;
import ru.thelper.models.user_procurement.UserProcurementLinks;
import ru.thelper.models.users.User;
import ru.thelper.repository.ProcurementRepository;
import ru.thelper.repository.UserProcurementLinksRepository;
import ru.thelper.repository.UserRepository;
import ru.thelper.services.exceptions.BadDataParsingException;
import ru.thelper.services.exceptions.DataManagerException;
import ru.thelper.services.mappers.ProcurementMapper;
import ru.thelper.services.models_sevices.user.CheckUserResult;
import ru.thelper.services.models_sevices.user.UserService;
import ru.thelper.services.parsers_dispatcher.Dispatcher;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class ProcurementServiceImpl implements ProcurementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementServiceImpl.class);

    public static final String REMODEL_DTO_TO_PROCUREMENT = "Remodel Dto to Procurement {}";

    public static final String DTO_WAS_REMODELED = "Dto was remodeled {}";

    public static final String ERROR_URL = "Error URL %s";

    public static final String NEED_ADD_TYPE = "Need add the procedure type: %s";

    public static final int FZ_615 = 615;

    public static final int FZ_44 = 44;

    public static final int FZ_223 = 223;

    public static final String DEADLINE_TIME_ZONE_LOGGER = "Deadline From String to ZonedDateTime. Deadline: {}, time zone: {}";

    public static final String ZONE_PARSING_LOGGER = "Time zone parsing {}";

    private final ProcurementRepository procurementRepository;

    private final UserRepository userRepository;

    private final UserProcurementLinksRepository userProcurementLinksRepository;

    private final Dispatcher dispatcher;

    private final UserService userService;

    private final ProcurementMapper procurementMapper;

    @Override
    public void save(ProcurementDto procurementDto) {
        var procurementFromBase = procurementRepository.getByUin(procurementDto.getUin());
        if (procurementFromBase.isEmpty()) {
            procurementRepository.save(remodelDtoToProcurement(procurementDto));
        }
    }

    @Override
    @Transactional
    public void save(UserDetails jwtUser, ProcurementDto procurementDto) {
        Procurement procurement;
        var procurementFromBase = procurementRepository.getByUin(procurementDto.getUin());
        if (procurementFromBase.isEmpty()) {
            procurement = procurementRepository.save(remodelDtoToProcurement(procurementDto));
        } else {
            procurement = procurementFromBase.orElseThrow(() -> new DataManagerException(DataManagerException.COULD_NOT_SAVE_PROCUREMENT));
        }
        User user = userRepository.findByUsername(jwtUser.getUsername()).orElseThrow(() -> new DataManagerException(DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION));
        userProcurementLinksRepository.save(UserProcurementLinks.builder().procurement(procurement).user(user).status(BaseStatus.ACTIVE).build());
        procurementRepository.save(procurement);
        userRepository.save(user);
    }

    protected Procurement remodelDtoToProcurement(ProcurementDto procurementDto) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(REMODEL_DTO_TO_PROCUREMENT, procurementDto);
        }
        var timeZone = stringToTimeZone(procurementDto.getTimeZone());
        var procurement = Procurement.builder()
                .uin(procurementDto.getUin())
                .applicationDeadline(remodelDeadlineFromStringToTime(procurementDto.getApplicationDeadline(), timeZone))
                .contractPrice(remodelPriceToBigDecimal(procurementDto.getContractPrice()))
                .applicationSecure(procurementDto.getApplicationSecure())
                .contractSecure(procurementDto.getContractSecure())
                .dateOfAuction(remodelDateOfAuctionToZonedDateTime(procurementDto.getDateOfAuction(), procurementDto.getTimeOfAuction(), timeZone))
                .dateOfPlacement(remodelDateOfPlacementToLocalDate(procurementDto.getDateOfPlacement()))
                .lastUpdatedFromEIS(remodelDateLastUpdateToLocalDate(procurementDto.getLastUpdatedFromEIS()))
                .dateTimeLastUpdated(LocalDate.now())
                .fzNumber(remodelFzToInteger(procurementDto.getFzNumber()))
                .linkOnPlacement(remodelStringUrlToURL(procurementDto.getLinkOnPlacement()))
                .objectOf(procurementDto.getObjectOf())
                .procedureType(remodelProcedureType(procurementDto.getProcedureType()))
                .publisherName(procurementDto.getPublisherName())
                .build();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(DTO_WAS_REMODELED, procurement);
        }
        return procurement;
    }

    @Override
    public void action(ProcurementAddress procurementAddress) {
        Procurement procurement = dispatcher.getFromUrl(procurementAddress.getAddress());
        Optional<Procurement> procurementFromDB = procurementRepository.getByUin(procurement.getUin());
        procurementFromDB.ifPresent(value -> BeanUtils.copyProperties(value, procurement, "id", "uin"));
        procurementRepository.save(procurement);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcurementDto> getProcurements(final User user, Pageable pageable) {
        if (userService.isUserCorrect(user) == CheckUserResult.INCORRECTED) {
            throw new BadRequestException("User incorrect.");
        }
        List<ProcurementDto> result = procurementRepository.getAllByUsersIsIn(Set.of(user), pageable).stream().map(procurementMapper::dboToDto).toList();
        return new PageImpl<>(result, pageable, result.size());
    }

    //ToDo: В будущем возможно сделать отельный сервис определения часовых поясов
    private TimeZone stringToTimeZone(String timeZone) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(ZONE_PARSING_LOGGER, timeZone);
        }
        if (timeZone == null || timeZone.isEmpty() || timeZone.isBlank()) {
            return TimeZone.getTimeZone("Europe/Moscow");
        }
        if (timeZone.toLowerCase().contains("владивосток") || timeZone.contains("МСК+7") || timeZone.contains("UTC+10")) {
            return TimeZone.getTimeZone("Asia/Vladivostok");
        }
        if (timeZone.contains("МСК+6") || timeZone.contains("UTC+9")) {
            return TimeZone.getTimeZone("Asia/Yakutsk");
        }
        if (timeZone.contains("МСК+5") || timeZone.contains("UTC+8")) {
            return TimeZone.getTimeZone("Asia/Irkutsk");
        }
        if (timeZone.contains("МСК+4") || timeZone.contains("UTC+7")) {
            return TimeZone.getTimeZone("Asia/Novosibirsk");
        }
        if (timeZone.contains("МСК+3") || timeZone.contains("UTC+6")) {
            return TimeZone.getTimeZone("Asia/Omsk");
        }
        if (timeZone.contains("МСК+2") || timeZone.contains("UTC+5")) {
            return TimeZone.getTimeZone("Asia/Yekaterinburg");
        }
        if (timeZone.contains("МСК+1") || timeZone.contains("UTC+4")) {
            return TimeZone.getTimeZone("Europe/Samara");
        }
        if (timeZone.contains("МСК-1") || timeZone.contains("UTC+2")) {
            return TimeZone.getTimeZone("MSK-01 - Kaliningrad");
        }
//Must be LAST!!!!!
        if (timeZone.contains("МСК") || timeZone.contains("UTC+3")) {
            return TimeZone.getTimeZone("Europe/Moscow");
        }
        return TimeZone.getTimeZone("Europe/Moscow");
    }

    protected ProcedureType remodelProcedureType(String procedureType) {
        if (procedureType == null || procedureType.isEmpty() || procedureType.isBlank()) {
            return ProcedureType.DEFAULT_NONAME_PROCEDURE;
        }
        var procedure = ProcedureType.get(procedureType);
        if (procedure == ProcedureType.DEFAULT_NONAME_PROCEDURE) {
            var msg = String.format(NEED_ADD_TYPE, procedureType);
            LOGGER.error(msg);
        }
        return procedure;
    }

    protected URL remodelStringUrlToURL(String linkOnPlacement) {
        try {
            return new URL(linkOnPlacement);
        } catch (MalformedURLException e) {
            var message = String.format(ERROR_URL, linkOnPlacement);
            LOGGER.error(message);
            throw new BadDataParsingException(message, e);
        }
    }

    protected int remodelFzToInteger(String fzNumber) {
        if (fzNumber.startsWith("615")) {
            return FZ_615;
        }
        if (fzNumber.startsWith("44")) {
            return FZ_44;
        }
        if (fzNumber.startsWith("223")) {
            return FZ_223;
        }
        return 0;
    }

    protected LocalDate remodelDateLastUpdateToLocalDate(String lastUpdatedFromEIS) {
        return getLocalDate(lastUpdatedFromEIS);
    }

    protected LocalDate remodelDateOfPlacementToLocalDate(String dateOfPlacement) {
        return getLocalDate(dateOfPlacement);
    }

    protected ZonedDateTime remodelDateOfAuctionToZonedDateTime(String dateOfAuction, String timeOfAuction, TimeZone timeZone) {
        if (dateOfAuction == null || dateOfAuction.isEmpty() || dateOfAuction.isBlank()) {
            return null;
        }
        return ZonedDateTime.of(LocalDateTime.of(getLocalDate(dateOfAuction), getLocalTime(timeOfAuction)), timeZone.toZoneId());
    }

    protected BigDecimal remodelPriceToBigDecimal(String contractPrice) {
        if (contractPrice == null || contractPrice.isEmpty() || contractPrice.isBlank()) {
            return null;
        }
        return new BigDecimal(contractPrice);
    }

    protected ZonedDateTime remodelDeadlineFromStringToTime(String applicationDeadline, TimeZone timeZone) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(DEADLINE_TIME_ZONE_LOGGER, applicationDeadline, timeZone);
        }
        if (applicationDeadline == null || applicationDeadline.isEmpty() || applicationDeadline.isBlank()) {
            return null;
        }
        var timeParts = applicationDeadline.substring(0, 10).split("\\.");
        return ZonedDateTime.of(Integer.parseInt(timeParts[2]), Integer.parseInt(timeParts[1]), Integer.parseInt(timeParts[0]), 0, 0, 0, 0, timeZone.toZoneId());
    }

    private LocalTime getLocalTime(String stringFormatTime) {
        if (stringFormatTime == null || stringFormatTime.isEmpty() || stringFormatTime.isBlank()) {
            return null;
        }
        var timeParts = stringFormatTime.split(" ")[0].split(":");
        return LocalTime.of(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
    }

    private LocalDate getLocalDate(String stringFormatDate) {
        if (stringFormatDate == null || stringFormatDate.isEmpty() || stringFormatDate.isBlank()) {
            return null;
        }
        var timeParts = stringFormatDate.substring(0, 10).split("\\.");
        return LocalDate.of(Integer.parseInt(timeParts[2]), Integer.parseInt(timeParts[1]), Integer.parseInt(timeParts[0]));
    }
}