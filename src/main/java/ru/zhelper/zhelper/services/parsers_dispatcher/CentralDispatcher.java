package ru.zhelper.zhelper.services.parsers_dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.models.ProcurementType;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;
import ru.zhelper.zhelper.services.exceptions.BadRequest;
import ru.zhelper.zhelper.services.parsers_dispatcher.parser.ZakupkiParser;
import ru.zhelper.zhelper.services.validator.URLValidator;

import java.util.Map;

@Service
public class CentralDispatcher implements Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(CentralDispatcher.class);
    private static final String BAD_URL = "Url is not valid {}";
    private static final String LAW_44_PARSER = "Law44";
    private static final String LAW_615_PARSER = "Law615";
    private static final String DISPATCHER = "Dispatcher url {}";
    private static final String PARSER_NOT_FOUND = "PARSER NOT FOUND.";
    private static final String ERROR_PARSING = "ERROR FROM PARSER: {}";

    private final Map<String, ZakupkiParser> parsers;
    private final URLValidator validator;

    public CentralDispatcher(Map<String, ZakupkiParser> parsers, URLValidator validator) {
        this.parsers = parsers;
        this.validator = validator;
    }

    @Override
    public Procurement getFromUrl(String url) {
        if (logger.isDebugEnabled()) {
            logger.debug(DISPATCHER, url);
        }
        if (!validator.isValidUrl(url)) {
            logger.error(BAD_URL, url);
            throw new BadRequest(BAD_URL);
        }
        ProcurementType law = null;
        try {
            law = validator.getProcurementType(url);
            if (law == ProcurementType.LAW_44) {
                return parsers.get(LAW_44_PARSER).parse(url);
            }

            if (law == ProcurementType.LAW_615) {
                return parsers.get(LAW_615_PARSER).parse(url);
            }
        } catch (BadDataParsingException exception) {
            logger.error(ERROR_PARSING, law, exception);
            throw new BadRequest(ERROR_PARSING, exception);
        }
        logger.error(PARSER_NOT_FOUND);
        throw new BadRequest(PARSER_NOT_FOUND);
    }
}
