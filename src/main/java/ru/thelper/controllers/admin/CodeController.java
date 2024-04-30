package ru.thelper.controllers.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.thelper.cfg.ApiVersion;
import ru.thelper.services.geting_code.TelegramCodeServiceImpl;
import ru.thelper.services.geting_code.UserIdTimed;


import java.util.Map;

import static ru.thelper.controllers.admin.CodeController.URL;


@RestController
@RequestMapping(URL)
@CrossOrigin(origins = "*", maxAge = 3600)

public class CodeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodeController.class);

    public static final String URL = ApiVersion.VERSION_1_0 + "/code";

    public static final String GET_ALL_CODES = "GetAll codes.";

    private final TelegramCodeServiceImpl telegramCodeService;

    public CodeController(TelegramCodeServiceImpl telegramCodeService) {
        this.telegramCodeService = telegramCodeService;
    }

    @GetMapping({"/codes"})
//    @RolesAllowed("ADMIN")
    public Map<Integer, UserIdTimed> getAll() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(GET_ALL_CODES);
        }
        return telegramCodeService.getAllCodes();
    }
}
