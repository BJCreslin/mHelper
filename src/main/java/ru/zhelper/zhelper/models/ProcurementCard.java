package ru.zhelper.zhelper.models;

import java.net.URL;
import java.util.GregorianCalendar;

public class ProcurementCard {
    private GregorianCalendar applicationDeadline;
    private long contractPrice;
    private String procedureType;
    private String customer;
    private String restrictions;
    private URL placementLink;
    private int applicationSecure;
    private int contractSecure;


    public ProcurementCard(GregorianCalendar applicationDeadline, URL placementLink, long contractPrice,
                           int applicationSecure, int contractSecure, String procedureType, String customer,
                           String restrictions) {
        this.applicationDeadline = applicationDeadline;
        this.placementLink = placementLink;
        this.contractPrice = contractPrice;
        this.applicationSecure = applicationSecure;
        this.contractSecure = contractSecure;
        this.procedureType = procedureType;
        this.customer = customer;
        this.restrictions = restrictions;
    }

    public GregorianCalendar getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(GregorianCalendar applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public URL getPlacementLink() {
        return placementLink;
    }

    public void setPlacementLink(URL placementLink) {
        this.placementLink = placementLink;
    }

    public long getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(long contractPrice) {
        this.contractPrice = contractPrice;
    }

    public int getApplicationSecure() {
        return applicationSecure;
    }

    public void setApplicationSecure(int applicationSecure) {
        this.applicationSecure = applicationSecure;
    }

    public int getContractSecure() {
        return contractSecure;
    }

    public void setContractSecure(int contractSecure) {
        this.contractSecure = contractSecure;
    }

    public String getProcedureType() {
        return procedureType;
    }

    public void setProcedureType(String procedureType) {
        this.procedureType = procedureType;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }
}
