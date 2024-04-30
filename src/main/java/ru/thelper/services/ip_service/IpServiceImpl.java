package ru.thelper.services.ip_service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;


@Service
public class IpServiceImpl implements IpService {

    private static final String HEADER_X_FORWARD = "X-Forwarded-For";

    private static final String COMMA_SEPARATOR = ",";

    @Override
    public String getIpFromRequest(HttpServletRequest request) {
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
