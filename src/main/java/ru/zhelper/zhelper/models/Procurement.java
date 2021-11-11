package ru.zhelper.zhelper.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "procurements")
public class Procurement implements Serializable {

    //Что означает поле
    //Где можно найти по ФЗ №44
    //и по ФЗ №223
    private static final long serialVersionUID = 8376509998615282286L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //Этап закупки
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Этап закупки
    //ЖУРНАЛ СОБЫТИЙ -> СОБЫТИЕ -> Закупка переведена на этап "stage"
    @Column(name = "stage")
    @Enumerated(EnumType.STRING)
    private Stage stage;

    //Уникальный идентификатор закупки
    //regNumber в ссылке на закупки или просто номер закупки
    //ОБЩИЕ СВЕДЕНИЯ О ЗАКУПКЕ -> Реестровый номер извещения
    @Column(name = "uin", unique = true, nullable = false, length = 30)
    private String uin;

    //Номер федерального закона
    //Можно вычислить в зависимости от длины номера uin, для ФЗ №44 19 знаков
    //для ФЗ №223 11 знаков
    @Column(name = "fz_number", nullable = false)
    private int fzNumber;

    //Дата и время окончания срока подачи заявок
    //ИНФОРМАЦИЯ О ПРОЦЕДУРЕ ЭЛЕКТРОННОГО АУКЦИОНА -> Дата и время окончания срока подачи заявок на участие в электронном аукционе
    //ПОРЯДОК ПРОВЕДЕНИЯ ПРОЦЕДУРЫ -> Дата и время окончания подачи заявок (по местному времени заказчика)
    @Column(name = "application_deadline")
    private ZonedDateTime applicationDeadline;

    //Максимальная цена
    //Начальная цена
    //СПИСОК ЛОТОВ -> СВЕДЕНИЯ О ЦЕНЕ ДОГОВОРА -> Начальная (максимальная) цена договора 
    @Column(name = "contract_price")
    private BigDecimal contractPrice;

    //Тип закупки
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Способ определения поставщика (подрядчика, исполнителя)
    //ОБЩИЕ СВЕДЕНИЯ О ЗАКУПКЕ -> Способ размещения закупки
    @Column(name = "procedure_type")
    @Enumerated(EnumType.STRING)
    private ProcedureType procedureType;

    //Кто опубликовал извещение о закупке
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Размещение осуществляет
    //ЗАКАЗЧИК -> Наименование организации
    @Column(name = "publisher_name", nullable = false)
    private String publisherName;

    //Есть ли ограничения по СМП
    //ПРЕИМУЩЕСТВА, ТРЕБОВАНИЯ К УЧАСТНИКАМ -> Ограничения и запреты
    //ТРЕБОВАНИЯ К УЧАСТНИКАМ ЗАКУПКИ или ПОРЯДОК ПРОВЕДЕНИЯ ПРОЦЕДУРЫ -> Порядок подведения итогов
    @Column(name = "restrictions")
    private String restrictions;

    //Ссылка на закупку на площадке размещения
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Адрес электронной площадки в информационно-телекоммуникационной сети "Интернет"
    //ОБЩИЕ СВЕДЕНИЯ О ЗАКУПКЕ -> Адрес электронной площадки в информационно-телекоммуникационной сети "Интернет"
    // TODO @URL(regexp = "^(http|ftp).*")
    @Column(name = "link_on_placement", nullable = false)
    private URL linkOnPlacement;

    //Обеспечение заявки
    //ОБЕСПЕЧЕНИЕ ЗАЯВКИ -> Обеспечение заявки (размер)
    // = см. документацию
    @Column(name = "application_secure")
    private String applicationSecure;

    //Обеспечение контракта
    //Обеспечение исполнения контракта (размер)
    // = см. документацию
    @Column(name = "contract_secure")
    private String contractSecure;

    //Объект закупки
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Наименование объекта закупки
    //ОБЩИЕ СВЕДЕНИЯ О ЗАКУПКЕ -> Наименование закупки
    @Lob
    @Column(name = "object_of", length = 100000)
    @Type(type = "org.hibernate.type.TextType")
    private String objectOf;

    //Поле нужно чтобы отслеживать на сайте закупок необходимость обновить данные
    //Дата размещения текущей редакции извещения
    @Column(name = "last_updated_from_eis")
    private LocalDate lastUpdatedFromEIS;

    //Поле нужно чтобы отслеживать необходимость обновить данные
    @Column(name = "date_time_last_updated")
    private LocalDate dateTimeLastUpdated;

    //Дата размещения процедуры
    @Column
    private LocalDate dateOfPlacement;

    //Дата и время аукциона
    @Column
    private ZonedDateTime dateOfAuction;
}
