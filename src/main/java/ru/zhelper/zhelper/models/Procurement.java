package ru.zhelper.zhelper.models;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;

public class Procurement {
    //Что означает поле
    //Где можно найти по ФЗ №44
    //и по ФЗ №223

    //Этап закупки
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Этап закупки
    //ЖУРНАЛ СОБЫТИЙ -> СОБЫТИЕ -> Закупка переведена на этап "stage"
    private Stage stage;

    //Уникальный идентификатор закупки
    //regNumber в ссылке на закупки или просто номер закупки
    //ОБЩИЕ СВЕДЕНИЯ О ЗАКУПКЕ -> Реестровый номер извещения
    private String uin;

    //Номер федерального закона
    //Можно вычислить в зависимости от длины номера uin, для ФЗ №44 19 знаков
    //для ФЗ №223 11 знаков
    private int fzNumber;

    //Дата и время окончания срока подачи заявок
    //ИНФОРМАЦИЯ О ПРОЦЕДУРЕ ЭЛЕКТРОННОГО АУКЦИОНА -> Дата и время окончания срока подачи заявок на участие в электронном аукционе
    //ПОРЯДОК ПРОВЕДЕНИЯ ПРОЦЕДУРЫ -> Дата и время окончания подачи заявок (по местному времени заказчика)
    private LocalDateTime applicationDeadline;

    //Максимальная цена
    //Начальная цена
    //СПИСОК ЛОТОВ -> СВЕДЕНИЯ О ЦЕНЕ ДОГОВОРА -> Начальная (максимальная) цена договора 
    private BigDecimal contractPrice;

    //Тип закупки
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Способ определения поставщика (подрядчика, исполнителя)
    //ОБЩИЕ СВЕДЕНИЯ О ЗАКУПКЕ -> Способ размещения закупки
    private ProcedureType procedureType;

    //Кто опубликовал извещение о закупке
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Размещение осуществляет
    //ЗАКАЗЧИК -> Наименование организации
    private String publisherName;

    //Есть ли ограничения по СМП
    //ПРЕИМУЩЕСТВА, ТРЕБОВАНИЯ К УЧАСТНИКАМ -> Ограничения и запреты
    //ТРЕБОВАНИЯ К УЧАСТНИКАМ ЗАКУПКИ или ПОРЯДОК ПРОВЕДЕНИЯ ПРОЦЕДУРЫ -> Порядок подведения итогов
    private String restrictions;

    //Ссылка на закупку на площадке размещения
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Адрес электронной площадки в информационно-телекоммуникационной сети "Интернет"
    //ОБЩИЕ СВЕДЕНИЯ О ЗАКУПКЕ -> Адрес электронной площадки в информационно-телекоммуникационной сети "Интернет"
    private URL linkOnPlacement;

    //Обеспечение заявки
    //ОБЕСПЕЧЕНИЕ ЗАЯВКИ -> Обеспечение заявки (размер)
    // = см. документацию
    private String applicationSecure;

    //Обеспечение контракта
    //Обеспечение исполнения контракта (размер)
    // = см. документацию
    private String contractSecure;

    //Объект закупки
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Наименование объекта закупки
    //ОБЩИЕ СВЕДЕНИЯ О ЗАКУПКЕ -> Наименование закупки
    private String objectOf;


    public Procurement(String uin, int fzNumber, Stage stage, LocalDateTime applicationDeadline, URL linkOnPlacement,
                       BigDecimal contractPrice, String applicationSecure, String contractSecure,
                       ProcedureType procedureType, String publisherName, String restrictions, String objectOf) {
        this.applicationDeadline = applicationDeadline;
        this.linkOnPlacement = linkOnPlacement;
        this.contractPrice = contractPrice;
        this.applicationSecure = applicationSecure;
        this.contractSecure = contractSecure;
        this.procedureType = procedureType;
        this.publisherName = publisherName;
        this.restrictions = restrictions;
        this.uin = uin;
        this.stage = stage;
        this.fzNumber = fzNumber;
        this.objectOf = objectOf;
    }

    public int getFzNumber() {
        return fzNumber;
    }

    public void setFzNumber(int fzNumber) {
        this.fzNumber = fzNumber;
    }

    public String getUin() {
        return uin;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public LocalDateTime getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDateTime applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public URL getLinkOnPlacement() {
        return linkOnPlacement;
    }

    public void setLinkOnPlacement(URL linkOnPlacement) {
        this.linkOnPlacement = linkOnPlacement;
    }

    public BigDecimal getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(BigDecimal contractPrice) {
        this.contractPrice = contractPrice;
    }

    public String getApplicationSecure() {
        return applicationSecure;
    }

    public void setApplicationSecure(String applicationSecure) {
        this.applicationSecure = applicationSecure;
    }

    public String getContractSecure() {
        return contractSecure;
    }

    public void setContractSecure(String contractSecure) {
        this.contractSecure = contractSecure;
    }

    public ProcedureType getProcedureType() {
        return procedureType;
    }

    public void setProcedureType(ProcedureType procedureType) {
        this.procedureType = procedureType;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public String getObjectOf() {
        return objectOf;
    }

    public void setObjectOf(String objectOf) {
        this.objectOf = objectOf;
    }
}
