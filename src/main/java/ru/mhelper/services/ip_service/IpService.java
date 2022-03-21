package ru.mhelper.services.ip_service;

import javax.servlet.http.HttpServletRequest;

public interface IpService {

    String getIpFromRequest(HttpServletRequest request);
}
