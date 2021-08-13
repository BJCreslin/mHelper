package ru.zhelper.zhelper.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class SimpleController {
    private static final String INDEX_PAGE_NAME ="zHelper";

    @GetMapping("/")
    public String get() {
        return INDEX_PAGE_NAME;
    }
}
