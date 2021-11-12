package ru.zhelper.zhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"chrome_auth"})
public class ChromeExtensionAuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChromeExtensionAuthController.class);

    @GetMapping({"", "/"})
    @ResponseBody
    public String testAuth() {
        return "hello";
    }
}
