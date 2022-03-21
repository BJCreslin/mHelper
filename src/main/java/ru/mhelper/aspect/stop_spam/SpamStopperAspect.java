package ru.mhelper.aspect.stop_spam;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mhelper.controllers.exeptions.BadRequestException;
import ru.mhelper.exceptions.SpamCodeException;
import ru.mhelper.services.ip_service.IpService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class SpamStopperAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpamStopperAspect.class);

    private static final Map<String, LocalTime> ipTable = new HashMap<>();

    public static final String ATTEMPTS = "Too many attempts from ip %s";

    public static final String ATTEMPT = "Attempt for enter from ip %s";

    public static final String REQUEST_IN_ARGS = "Bad request in StopSpam Aspect, no find HttpServletRequest in args.";

    private final IpService ipService;

    @Value("${spam.protected.range:12}")
    private Long spamProtectedRange;

    public SpamStopperAspect(IpService ipService) {
        this.ipService = ipService;
    }

    @Before("@annotation(ru.mhelper.aspect.stop_spam.StopSpam)")
    private void createStopSpam(JoinPoint joinPoint) {
        refreshIpTable();
        HttpServletRequest request = getHttpServletRequest(joinPoint);
        var ip = ipService.getIpFromRequest(request);
        if (LOGGER.isDebugEnabled()) {
            final var message = String.format(ATTEMPT, ip);
            LOGGER.debug(message);
        }
        if (ipTable.containsKey(ip)) {
            if (ipTable.get(ip).plusSeconds(spamProtectedRange).isAfter(LocalTime.now())) {
                final var message = String.format(ATTEMPTS, ip);
                LOGGER.info(message);
                throw new SpamCodeException(message);
            }
        } else {
            ipTable.put(ip, LocalTime.now());
        }
    }

    private void refreshIpTable() {
        if (ipTable.isEmpty()) {
            return;
        }
        ipTable.entrySet().stream().filter(x -> x.getValue().plusSeconds(spamProtectedRange).isBefore(LocalTime.now())).forEach(x -> ipTable.remove(x.getKey()));
    }

    private HttpServletRequest getHttpServletRequest(JoinPoint joinPoint) {
        for (Object mayBeRequest : joinPoint.getArgs()) {
            if (mayBeRequest instanceof HttpServletRequest) {
                return (HttpServletRequest) mayBeRequest;
            }
        }
        LOGGER.error(REQUEST_IN_ARGS);
        throw new BadRequestException(REQUEST_IN_ARGS);
    }
}
