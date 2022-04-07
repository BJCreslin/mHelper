package ru.mhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.mhelper.cfg.ApiVersion;
import ru.mhelper.controllers.exeptions.BadRequestException;
import ru.mhelper.models.dto.Error;
import ru.mhelper.models.dto.MessageResponse;
import ru.mhelper.models.dto.ProcurementDto;
import ru.mhelper.services.chrome.ProcurementDtoService;
import ru.mhelper.services.exceptions.BadDataParsingException;

import javax.validation.Valid;
import java.util.stream.Collectors;

import static ru.mhelper.controllers.ChromeExtensionController.URL;

@RestController
@RequestMapping(URL)
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChromeExtensionController {

    private static final Logger logger = LoggerFactory.getLogger(ChromeExtensionController.class);

    public static final String URL = ApiVersion.VERSION_1_0 + "/chrome";

    private static final String POST_FROM_IP = "Post from, procurement {}";

    private static final String ERROR_FROM_PARSING = "Error parsing";

    private static final String PROCUREMENT_IS_INVALID = "Procurement is invalid.";

    private static final String PROCUREMENT_WAS_SAVED = "Procurement was saved";

    public static final String SUCCESSFUL_CONNECTION = "Successful connection";

    private final ProcurementDtoService service;

    public ChromeExtensionController(ProcurementDtoService service) {
        this.service = service;
    }

    @PostMapping(value = {""}, consumes = {"application/json"})
    @Validated
    public ResponseEntity<?> newProcurement(@Valid @RequestBody ProcurementDto procurementDto, @AuthenticationPrincipal UserDetails user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getFieldErrors();
            var message = errors.stream().map(error -> "@" + error.getField().toUpperCase() + ": " + error.getDefaultMessage()).collect(Collectors.toList()).toString();
            var responseObject = Error.builder()
                .code(MessageResponse.BAD_CODE)
                .message(PROCUREMENT_IS_INVALID)
                .cause(message).build();
            logger.error(message);
            return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(POST_FROM_IP, procurementDto);
        }
        try {
            service.save(user, procurementDto);
            var success = new MessageResponse();
            success.setMessage(PROCUREMENT_WAS_SAVED);
            success.setCode(MessageResponse.FINE_CODE);
            return new ResponseEntity<>(success, HttpStatus.CREATED);
        } catch (BadDataParsingException exception) {
            logger.error(ERROR_FROM_PARSING, exception);
            throw new BadRequestException(ERROR_FROM_PARSING, exception);
        }
    }

    @GetMapping({""})
    @ResponseBody
    public ResponseEntity<?> testConnect() {
        return ResponseEntity.ok(new MessageResponse(SUCCESSFUL_CONNECTION));
    }
}
