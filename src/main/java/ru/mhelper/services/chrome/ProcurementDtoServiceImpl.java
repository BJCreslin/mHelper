package ru.mhelper.services.chrome;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.mhelper.models.BaseStatus;
import ru.mhelper.models.dto.ProcurementDto;
import ru.mhelper.models.procurements.ProcedureType;
import ru.mhelper.models.procurements.Procurement;
import ru.mhelper.models.user_procurement.UserProcurementLinks;
import ru.mhelper.models.users.User;
import ru.mhelper.repository.ProcurementRepository;
import ru.mhelper.repository.UserProcurementRepository;
import ru.mhelper.repository.UserRepository;
import ru.mhelper.services.exceptions.BadDataParsingException;
import ru.mhelper.services.exceptions.DataManagerException;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

@Service
public class ProcurementDtoServiceImpl implements ProcurementDtoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcurementDtoServiceImpl.class);

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

    private final UserProcurementRepository userProcurementRepository;

    public ProcurementDtoServiceImpl(ProcurementRepository procurementRepository, UserRepository userRepository, UserProcurementRepository userProcurementRepository) {
        this.procurementRepository = procurementRepository;
        this.userRepository = userRepository;
        this.userProcurementRepository = userProcurementRepository;
    }

    @Override
    public void save(ProcurementDto procurementDto) {
        var procurementFromBase = procurementRepository.getByUin(procurementDto.getUin());
        if (procurementFromBase.isEmpty()) {
            procurementRepository.save(remodelDtoToProcurement(procurementDto));
        }
    }

    @Override
    public void save(UserDetails jwtUser, ProcurementDto procurementDto) {
        Procurement procurement;
        var procurementFromBase = procurementRepository.getByUin(procurementDto.getUin());
        if (procurementFromBase.isEmpty()) {
            procurement = procurementRepository.save(remodelDtoToProcurement(procurementDto));
        } else {
            procurement = procurementFromBase.orElseThrow(() -> new DataManagerException(DataManagerException.COULD_NOT_SAVE_PROCUREMENT));
        }
        User user = userRepository.findByUsername(jwtUser.getUsername()).orElseThrow(() -> new DataManagerException(DataManagerException.NON_EXISTING_LOAD_OR_DELETE_EXCEPTION));
        userProcurementRepository.save(UserProcurementLinks.builder().procurement(procurement).user(user).status(BaseStatus.ACTIVE).build());
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

    //ToDo: ?? ?????????????? ???????????????? ?????????????? ???????????????? ???????????? ?????????????????????? ?????????????? ????????????
    private TimeZone stringToTimeZone(String timeZone) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(ZONE_PARSING_LOGGER, timeZone);
        }
        if (timeZone == null || timeZone.isEmpty() || timeZone.isBlank()) {
            return TimeZone.getTimeZone("Europe/Moscow");
        }
        if (timeZone.toLowerCase().contains("??????????????????????") || timeZone.contains("??????+7") || timeZone.contains("UTC+10")) {
            return TimeZone.getTimeZone("Asia/Vladivostok");
        }
        if (timeZone.contains("??????+6") || timeZone.contains("UTC+9")) {
            return TimeZone.getTimeZone("Asia/Yakutsk");
        }
        if (timeZone.contains("??????+5") || timeZone.contains("UTC+8")) {
            return TimeZone.getTimeZone("Asia/Irkutsk");
        }
        if (timeZone.contains("??????+4") || timeZone.contains("UTC+7")) {
            return TimeZone.getTimeZone("Asia/Novosibirsk");
        }
        if (timeZone.contains("??????+3") || timeZone.contains("UTC+6")) {
            return TimeZone.getTimeZone("Asia/Omsk");
        }
        if (timeZone.contains("??????+2") || timeZone.contains("UTC+5")) {
            return TimeZone.getTimeZone("Asia/Yekaterinburg");
        }
        if (timeZone.contains("??????+1") || timeZone.contains("UTC+4")) {
            return TimeZone.getTimeZone("Europe/Samara");
        }
        if (timeZone.contains("??????-1") || timeZone.contains("UTC+2")) {
            return TimeZone.getTimeZone("MSK-01 - Kaliningrad");
        }
//Must be LAST!!!!!
        if (timeZone.contains("??????") || timeZone.contains("UTC+3")) {
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