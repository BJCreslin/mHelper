package ru.zhelper.zhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/chrome_auth"})
public class ChromeExtensionAuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChromeExtensionAuthController.class);

}
