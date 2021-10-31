package ru.zhelper.zhelper.services.chrome;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.zhelper.zhelper.models.ProcedureType;
import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.models.dto.ProcurementDto;
import ru.zhelper.zhelper.services.dao.ProcurementDataManager;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;


@Service
public class ProcurementDtoServiceImpl implements ProcurementDtoService {
    private static final Logger logger = LoggerFactory.getLogger(ProcurementDtoServiceImpl.class);
    public static final String REMODEL_DTO_TO_PROCUREMENT = "Remodel Dto to Procurement {}";
    public static final String DTO_WAS_REMODELED = "Dto was remodeled {}";
    private final ProcurementDataManager procurementDataManager;

    public ProcurementDtoServiceImpl(ProcurementDataManager procurementDataManager) {
        this.procurementDataManager = procurementDataManager;
    }

    @Override
    public void save(ProcurementDto procurementDto) {
        var procurementFromBase = procurementDataManager.loadByUin(procurementDto.getUin());
        if (procurementFromBase.isEmpty()) {
            procurementDataManager.save(remodelDtoToProcurement(procurementDto));
        }
    }

    protected Procurement remodelDtoToProcurement(ProcurementDto procurementDto) {
        if (logger.isDebugEnabled()) {
            logger.debug(REMODEL_DTO_TO_PROCUREMENT, procurementDto);
        }
        var procurement = Procurement.builder()
                .uin(procurementDto.getUin())
                .applicationDeadline(remodelDeadlineFromStringToTime(procurementDto.getApplicationDeadline()))
                .contractPrice(remodelPriceToBigDecimal(procurementDto.getContractPrice()))
                .applicationSecure(procurementDto.getApplicationSecure())
                .contractSecure(procurementDto.getContractSecure())
                .dateOfAuction(remodelDateOfAuctionToZonedDateTime(procurementDto.getDateOfAuction()))
                .dateOfPlacement(remodelDateOfPlacementToLocalDate(procurementDto.getDateOfPlacement()))
                .lastUpdatedFromEIS(remodelDateLastUpdateToLocalDate(procurementDto.getLastUpdatedFromEIS()))
                .dateTimeLastUpdated(LocalDate.now())
                .fzNumber(remodelFztoInteger(procurementDto.getFzNumber()))
                .linkOnPlacement(remodelStringUrlToURL(procurementDto.getLinkOnPlacement()))
                .objectOf(procurementDto.getObjectOf())
                .procedureType(remodelProcedureType(procurementDto.getProcedureType()))
                .publisherName(procurementDto.getPublisherName())
                .build();
        if (logger.isDebugEnabled()) {
            logger.debug(DTO_WAS_REMODELED, procurement);
        }
        return procurement;
    }

    protected ProcedureType remodelProcedureType(String procedureType) {
        return null;
    }

    protected URL remodelStringUrlToURL(String linkOnPlacement) {
        try {
            return new URL(linkOnPlacement);
        } catch (MalformedURLException e) {
            var message = String.format("Error URL %s", linkOnPlacement);
            logger.error(message);
            throw new BadDataParsingException(message, e);
        }
    }

    protected int remodelFztoInteger(String fzNumber) {
        return 0;
    }

    protected LocalDate remodelDateLastUpdateToLocalDate(String lastUpdatedFromEIS) {
        return null;
    }

    protected LocalDate remodelDateOfPlacementToLocalDate(String dateOfPlacement) {
        return null;
    }

    protected ZonedDateTime remodelDateOfAuctionToZonedDateTime(String dateOfAuction) {
        return null;
    }

    protected BigDecimal remodelPriceToBigDecimal(String contractPrice) {
        return null;
    }

    protected ZonedDateTime remodelDeadlineFromStringToTime(String applicationDeadline) {
        return null;
    }
}
