package ru.zhelper.zhelper.models;

public enum BaseStatus {
    ACTIVE("ACTIVE"),
    NOT_ACTIVE("NOT_ACTIVE"),
    DELETED("DELETED"),
    BANNED("BANNED");

    public final String status;

    public String getStatus() {
        return status;
    }

    BaseStatus(String status) {
        this.status = status;
    }
}
