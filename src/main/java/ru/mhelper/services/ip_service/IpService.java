package ru.mhelper.services.ip_service;


import jakarta.servlet.http.HttpServletRequest;

public interface IpService {

    String getIpFromRequest(HttpServletRequest request);
}
