package ru.mhelper.models.procurements;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.mhelper.models.BaseEntity;
import ru.mhelper.models.user_procurement.UserProcurementLinks;
import ru.mhelper.models.users.User;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Builder
@Table(name = "procurements")
public class Procurement extends BaseEntity implements Serializable {

    //Что означает поле
    //Где можно найти по ФЗ №44
    //и по ФЗ №223
    @Serial
    private static final long serialVersionUID = 8376509998615282286L;

    public static final String DEADLINE = "application_deadline";

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
    @Column(name = DEADLINE)
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
    @Column(columnDefinition = "clob", name = "object_of")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_procurements",
        joinColumns = @JoinColumn(name = "procurement_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    @OneToMany(mappedBy = "procurement", orphanRemoval = true)
    @Builder.Default
    private Set<UserProcurementLinks> userProcurementLinkses = new LinkedHashSet<>();

    public Procurement() {
        this.users = new HashSet<>();
        this.userProcurementLinkses = new LinkedHashSet<>();
    }

    public Procurement(Stage stage, String uin, int fzNumber, ZonedDateTime applicationDeadline, BigDecimal contractPrice, ProcedureType procedureType, String publisherName, String restrictions, URL linkOnPlacement, String applicationSecure, String contractSecure, String objectOf, LocalDate lastUpdatedFromEIS, LocalDate dateTimeLastUpdated, LocalDate dateOfPlacement, ZonedDateTime dateOfAuction, Set<User> users, Set<UserProcurementLinks> userProcurementLinkses) {
        this.stage = stage;
        this.uin = uin;
        this.fzNumber = fzNumber;
        this.applicationDeadline = applicationDeadline;
        this.contractPrice = contractPrice;
        this.procedureType = procedureType;
        this.publisherName = publisherName;
        this.restrictions = restrictions;
        this.linkOnPlacement = linkOnPlacement;
        this.applicationSecure = applicationSecure;
        this.contractSecure = contractSecure;
        this.objectOf = objectOf;
        this.lastUpdatedFromEIS = lastUpdatedFromEIS;
        this.dateTimeLastUpdated = dateTimeLastUpdated;
        this.dateOfPlacement = dateOfPlacement;
        this.dateOfAuction = dateOfAuction;
        if (Objects.isNull(users)) {
            this.users = new HashSet<>();
        } else {
            this.users = users;
        }
        if (Objects.isNull(userProcurementLinkses)) {
            this.userProcurementLinkses = new LinkedHashSet<>();
        } else {
            this.userProcurementLinkses = userProcurementLinkses;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Procurement that = (Procurement) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
