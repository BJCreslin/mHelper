package ru.mhelper.controllers.admin;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mhelper.cfg.ApiVersion;
import ru.mhelper.services.geting_code.TelegramCodeServiceImpl;
import ru.mhelper.services.geting_code.UserIdTimed;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

import static ru.mhelper.controllers.AuthController.URL;

@RestController
@RequestMapping(URL)
@CrossOrigin(origins = "*", maxAge = 3600)
@OpenAPIDefinition(info = @Info(title = "Telegram code API", version = "1.0", description = "Telegram codes Information"))
public class CodeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodeController.class);

    public static final String URL = ApiVersion.VERSION_1_0 + "/code";

    public static final String GET_ALL_CODES = "GetAll codes.";

    private final TelegramCodeServiceImpl telegramCodeService;

    public CodeController(TelegramCodeServiceImpl telegramCodeService) {
        this.telegramCodeService = telegramCodeService;
    }

    @GetMapping({"/codes"})
    @RolesAllowed("ADMIN")
    public Map<Integer, UserIdTimed> getAll() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(GET_ALL_CODES);
        }
        return telegramCodeService.getAllCodes();
    }
}
