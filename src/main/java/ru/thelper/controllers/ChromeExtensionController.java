package ru.thelper.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ru.thelper.cfg.ApiVersion;
import ru.thelper.models.dto.Error;
import ru.thelper.models.dto.MessageResponse;
import ru.thelper.models.dto.ProcurementDto;
import ru.thelper.services.chrome.ProcurementService;
import ru.thelper.services.metrics.MetricsService;

import java.util.stream.Collectors;

import static ru.thelper.controllers.ChromeExtensionController.URL;

@RestController
@RequestMapping(URL)
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ChromeExtensionController {

    private static final Logger logger = LoggerFactory.getLogger(ChromeExtensionController.class);

    public static final String URL = ApiVersion.VERSION_1_0 + "/chrome";

    private static final String POST_FROM_IP = "Post from, procurement {}";

    private static final String PROCUREMENT_IS_INVALID = "Procurement is invalid.";

    private static final String PROCUREMENT_WAS_SAVED = "Procurement was saved";

    public static final String SUCCESSFUL_CONNECTION = "Successful connection";

    private final ProcurementService service;

    private final MetricsService metricsService;

    @PostMapping(value = {""}, consumes = {"application/json"})
    @Validated
    public ResponseEntity<?> newProcurement(@Valid @RequestBody ProcurementDto procurementDto, @AuthenticationPrincipal UserDetails user, BindingResult bindingResult) {
        metricsService.incrementApiNewProcurements();
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
    public ResponseEntity<MessageResponse> testConnect() {
        return ResponseEntity.ok(new MessageResponse(SUCCESSFUL_CONNECTION));
    }
}
