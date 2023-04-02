package ru.mhelper.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mhelper.controllers.exeptions.BadRequestException;
import ru.mhelper.models.dto.ProcurementAddress;
import ru.mhelper.models.procurements.Procurement;
import ru.mhelper.repository.ProcurementRepository;
import ru.mhelper.services.exceptions.BadDataParsingException;
import ru.mhelper.services.ip_service.IpService;
import ru.mhelper.services.procurement.ProcurementService;

import java.util.List;

import static ru.mhelper.controllers.SimpleController.INDEX_PAGE_NAME;

@Controller
@RequestMapping({"", INDEX_PAGE_NAME})
@CrossOrigin
public class SimpleController {

    private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);

    public static final String INDEX_PAGE_NAME = "/mHelper";

    private static final String GET_FROM_IP = "Get from ip {}";

    private static final String POST_FROM_IP = "Post from ip {}, procurement {}";

    private static final String POSTED_PROCUREMENT = "Procurement {} posted.";

    private static final String ERROR_FROM_PARSING = "Error parsing";

    private static final String EMPTY_ADDRESS = "";

    private final ProcurementRepository repo;

    private final ProcurementService service;

    private final IpService ipService;


    public SimpleController(ProcurementRepository repo, ProcurementService service, IpService ipService) {
        this.repo = repo;
        this.service = service;
        this.ipService = ipService;
    }

    @GetMapping("")
    public String get(HttpServletRequest request, Model model) {
        if (logger.isDebugEnabled()) {
            logger.debug(GET_FROM_IP, ipService.getIpFromRequest(request));
        }
        List<Procurement> procurements = repo.findAll();
        model.addAttribute("procurements", procurements);
        model.addAttribute("address", ProcurementAddress.builder().address(EMPTY_ADDRESS).build());
        return INDEX_PAGE_NAME;
    }

    @PostMapping("")
    public String post(HttpServletRequest request, Model model, @ModelAttribute("address") ProcurementAddress address) {
        if (logger.isDebugEnabled()) {
            logger.debug(POST_FROM_IP, ipService.getIpFromRequest(request), address);
        }
        try {
            service.action(address);
        } catch (BadDataParsingException exception) {
            logger.error(ERROR_FROM_PARSING, exception);
            throw new BadRequestException(ERROR_FROM_PARSING, exception);
        }
        List<Procurement> procurements = repo.findAll();
        model.addAttribute("procurements", procurements);
        model.addAttribute("address", ProcurementAddress.builder().address(EMPTY_ADDRESS).build());
        if (logger.isDebugEnabled()) {
            logger.debug(POSTED_PROCUREMENT, address);
        }
        return INDEX_PAGE_NAME;
    }
}
