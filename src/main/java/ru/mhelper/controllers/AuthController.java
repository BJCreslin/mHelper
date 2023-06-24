package ru.mhelper.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mhelper.aspect.stop_spam.StopSpam;
import ru.mhelper.cfg.ApiVersion;
import ru.mhelper.models.dto.*;
import ru.mhelper.security.auth.AuthService;
import ru.mhelper.services.geting_code.TelegramCodeService;

import static ru.mhelper.controllers.AuthController.URL;

@RestController
@RequestMapping(URL)
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    public static final String URL = ApiVersion.VERSION_1_0 + "/auth";

    public static final String TEST_JWT = "/test";

    public static final String CODE_URL = "/code/";

    public static final String AUTHENTICATING = "User with UserName {} is authenticating";

    public static final String CODE_AUTHENTICATING = "User with code {} is authenticating";


    public static final String SUCCESSFUL_CONNECTION = "Successful connection";

    public static final String CODE_NOT_FOUND = "Code not found";

    private final TelegramCodeService telegramCodeService;

    private final AuthService authService;


    @GetMapping({""})
    public ResponseEntity<MessageResponse> testConnect() {
        return ResponseEntity.ok(new MessageResponse(SUCCESSFUL_CONNECTION));
    }

//    @PreAuthorize("hasAnyRole('ADMIN', 'CHROME_EXTENSION')")
    @GetMapping({TEST_JWT})
    public ResponseEntity<MessageResponse> testAuthConnect() {
        return ResponseEntity.ok(new MessageResponse(SUCCESSFUL_CONNECTION));
    }

    @GetMapping({CODE_URL + "{code}"})
    @StopSpam
    public Object tgSignIn(@PathVariable Integer code, HttpServletRequest request) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(CODE_AUTHENTICATING, code);
        }
        if (telegramCodeService.existByCode(code)) {
            return authService.getResponseEntity(code);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse(MessageResponse.BAD_TELEGRAM_CODE, CODE_NOT_FOUND));
        }
    }

    @PostMapping({"/signin"})
    public ResponseEntity<JwtResponse> signIn(@RequestBody LoginRequest loginRequest) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(AUTHENTICATING, loginRequest.getUserName());
        }
        return authService.doSignIn(loginRequest);
    }

    @PostMapping(value = {"/signup"}, consumes = {"application/json"})
    public ResponseEntity<AbstractResponse> registerUser(@RequestBody SignUpRequest signupRequest) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Create new User with Name {}, email {}", signupRequest.getUserName(), signupRequest.getEmail());
        }
        return authService.doSignUp(signupRequest);
    }

}
