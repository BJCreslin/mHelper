package ru.zhelper.zhelper.models;

public enum Stage {
    SUBMISSION_OF_APPLICATION("Подача заявок"),
    WORK_OF_THE_COMMISSION("Работа комиссии"),
    PROCUREMENT_ENDED("Закупка завершена"),
    PROCUREMENT_CANCELED("Закупка отменена");

    private String title;

    public String getTitle() {
        return title;
    }

    Stage(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
