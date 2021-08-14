package ru.zhelper.zhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("")
public class SimpleController {
    private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);

    private static final String INDEX_PAGE_NAME = "zHelper";
    private static final String COMMA = ",";
    private static final String HEADER_X_FORWARD = "X-Forwarded-For";
    private static final String GET_FROM_IP = "Get from ip {}";
    private static final String POST_FROM_IP = "Post from ip {}, procurement {}";
    private static final String POSTED_PROCUREMENT = "Procurement {} posted.";

    @GetMapping("/")
    public String get(HttpServletRequest request, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug(GET_FROM_IP, getIpFromRequest(request));
        }
        // todo: <---------------Insert getAll from Base ----------------->
        return INDEX_PAGE_NAME;
    }

    @PostMapping("/")
    public String post(HttpServletRequest request, Model model, @RequestParam("procurement") String procurement) {
        if (logger.isDebugEnabled()) {
            logger.debug(POST_FROM_IP, getIpFromRequest(request), procurement);
        }
        // todo: <---------------Insert Save to base ----------------->
        // todo: <---------------Insert getAll from Base ----------------->
        if (logger.isDebugEnabled()) {
            logger.debug(POSTED_PROCUREMENT, procurement);
        }
        return INDEX_PAGE_NAME;
    }

    private String getIpFromRequest(HttpServletRequest request) {
        String ip;
        if (request.getHeader(HEADER_X_FORWARD) != null) {
            String xForwardedFor = request.getHeader(HEADER_X_FORWARD);
            if (xForwardedFor.contains(COMMA)) {
                ip = xForwardedFor.substring(xForwardedFor.lastIndexOf(COMMA) + 2);
            } else {
                ip = xForwardedFor;
            }
        } else {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}