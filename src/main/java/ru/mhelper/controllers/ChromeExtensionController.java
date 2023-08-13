package ru.mhelper.controllers;

import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
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
import ru.mhelper.models.dto.Error;
import ru.mhelper.models.dto.MessageResponse;
import ru.mhelper.models.dto.ProcurementDto;
import ru.mhelper.services.chrome.ProcurementService;

import java.util.stream.Collectors;

import static ru.mhelper.controllers.ChromeExtensionController.URL;

@RestController
@RequestMapping(URL)
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChromeExtensionController {

    private static final Logger logger = LoggerFactory.getLogger(ChromeExtensionController.class);

    public static final String URL = ApiVersion.VERSION_1_0 + "/chrome";

    private static final String POST_FROM_IP = "Post from, procurement {}";

    private static final String PROCUREMENT_IS_INVALID = "Procurement is invalid.";

    private static final String PROCUREMENT_WAS_SAVED = "Procurement was saved";

    public static final String SUCCESSFUL_CONNECTION = "Successful connection";

    private final ProcurementService service;

    public ChromeExtensionController(ProcurementService service) {
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
            service.save(user, procurementDto);
            MessageResponse success = getSuccessMessageResponse();
            return new ResponseEntity<>(success, HttpStatus.CREATED);
    }

    @NotNull
    private static MessageResponse getSuccessMessageResponse() {
        var success = new MessageResponse();
        success.setMessage(PROCUREMENT_WAS_SAVED);
        success.setCode(MessageResponse.FINE_CODE);
        return success;
    }

    @GetMapping({""})
    @ResponseBody
    public ResponseEntity<MessageResponse> testConnect() {
        return ResponseEntity.ok(new MessageResponse(SUCCESSFUL_CONNECTION));
    }
}
