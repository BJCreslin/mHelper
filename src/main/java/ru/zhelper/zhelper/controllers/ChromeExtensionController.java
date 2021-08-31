package ru.zhelper.zhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.zhelper.zhelper.controllers.exeptions.BadRequestException;
import ru.zhelper.zhelper.models.dto.ProcurementDto;
import ru.zhelper.zhelper.services.ProcurementService;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;

@RestController
@RequestMapping({"/api"})
@CrossOrigin
public class ChromeExtensionController {
    private static final Logger logger = LoggerFactory.getLogger(ChromeExtensionController.class);
    private static final String POST_FROM_IP = "Post from, procurement {}";
    private static final String POSTED_PROCUREMENT = "Procurement {} posted.";
    private static final String ERROR_FROM_PARSING = "Error parsing";
    private final ProcurementService service;

    public ChromeExtensionController(ProcurementService service) {
        this.service = service;
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void newProcurement(@ModelAttribute("procurementDto") ProcurementDto procurementDto) {
        if (logger.isDebugEnabled()) {
            logger.debug(POST_FROM_IP, procurementDto);
        }
        try {
            //service.action(procurementDto);
            logger.debug(procurementDto.toString());
        } catch (BadDataParsingException exception) {
            logger.error(ERROR_FROM_PARSING, exception);
            throw new BadRequestException(ERROR_FROM_PARSING, exception);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(POSTED_PROCUREMENT, procurementDto);
        }
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String gProcurement() {
        return "dsdsds";
    }
}
