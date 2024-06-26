package ru.thelper.aspect.stop_spam;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.thelper.exceptions.BadRequestException;
import ru.thelper.exceptions.SpamCodeException;
import ru.thelper.services.ip_service.IpService;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class SpamStopperAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpamStopperAspect.class);

    private static final Map<String, LocalTime> ipTable = new ConcurrentHashMap<>();

    public static final String ATTEMPTS = "Too many attempts from ip %s";

    public static final String ATTEMPT = "Attempt for enter from ip %s";

    public static final String REQUEST_IN_ARGS = "Bad request in StopSpam Aspect, no find HttpServletRequest in args.";

    private final IpService ipService;

    @Value("${spam.protected.range:1}")
    private Long spamProtectedRange;

    public SpamStopperAspect(IpService ipService) {
        this.ipService = ipService;
    }

    @Before("@annotation(ru.thelper.aspect.stop_spam.StopSpam)")
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
        Set<String> ipsForDelete = new HashSet<>();
        ipTable.entrySet().stream().filter(x -> x.getValue().plusSeconds(spamProtectedRange).isBefore(LocalTime.now())).forEach(x -> ipsForDelete.add(x.getKey()));
        if (ipsForDelete.isEmpty()) {
            return;
        }
        ipsForDelete.forEach(ipTable::remove);
    }

    private HttpServletRequest getHttpServletRequest(JoinPoint joinPoint) {
        for (Object mayBeRequest : joinPoint.getArgs()) {
            if (mayBeRequest instanceof HttpServletRequest httpServletRequest) {
                return httpServletRequest;
            }
        }
        LOGGER.error(REQUEST_IN_ARGS);
        throw new BadRequestException(REQUEST_IN_ARGS);
    }
}
