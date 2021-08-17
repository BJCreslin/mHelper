package ru.zhelper.zhelper.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.models.dto.ProcurementAddress;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("")
public class SimpleController {
    private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);

    private static final String INDEX_PAGE_NAME = "zHelper";
    private static final String COMMA_SEPARATOR = ",";
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
     //   List<Procurement> procurements = Collections.emptyList();
      //  model.addAttribute("procurements", procurements);
        return INDEX_PAGE_NAME;
    }

    @PostMapping("/")
    public String post(HttpServletRequest request, Model model, @ModelAttribute("procurementAddress") ProcurementAddress address) {
        if (logger.isDebugEnabled()) {
            logger.debug(POST_FROM_IP, getIpFromRequest(request), address);
        }
        // todo: <---------------Insert Save to base ----------------->
        // todo: <---------------Insert getAll from Base ----------------->
    //    List<Procurement> procurements = Collections.emptyList();
     //   model.addAttribute("procurements", procurements);
        if (logger.isDebugEnabled()) {
            logger.debug(POSTED_PROCUREMENT, address);
        }
        return INDEX_PAGE_NAME;
    }

    private String getIpFromRequest(HttpServletRequest request) {
        String ip;
        if (request.getHeader(HEADER_X_FORWARD) != null) {
            String xForwardedFor = request.getHeader(HEADER_X_FORWARD);
            if (xForwardedFor.contains(COMMA_SEPARATOR)) {
                ip = xForwardedFor.substring(xForwardedFor.lastIndexOf(COMMA_SEPARATOR) + 2);
            } else {
                ip = xForwardedFor;
            }
        } else {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
