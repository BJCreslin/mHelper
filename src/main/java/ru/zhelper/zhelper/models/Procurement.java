package ru.zhelper.zhelper.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Procurement implements Serializable {
    
	//Что означает поле
    //Где можно найти по ФЗ №44
    //и по ФЗ №223
	private static final long serialVersionUID = -3159634854548811691L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "serial")
    private long id;
    
	//Этап закупки
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Этап закупки
    //ЖУРНАЛ СОБЫТИЙ -> СОБЫТИЕ -> Закупка переведена на этап "stage"
    @Column
    private Stage stage;

    //Уникальный идентификатор закупки
    //regNumber в ссылке на закупки или просто номер закупки
    //ОБЩИЕ СВЕДЕНИЯ О ЗАКУПКЕ -> Реестровый номер извещения
    @Column
    private String uin;

    //Номер федерального закона
    //Можно вычислить в зависимости от длины номера uin, для ФЗ №44 19 знаков
    //для ФЗ №223 11 знаков
    @Column
    private int fzNumber;

    //Дата и время окончания срока подачи заявок
    //ИНФОРМАЦИЯ О ПРОЦЕДУРЕ ЭЛЕКТРОННОГО АУКЦИОНА -> Дата и время окончания срока подачи заявок на участие в электронном аукционе
    //ПОРЯДОК ПРОВЕДЕНИЯ ПРОЦЕДУРЫ -> Дата и время окончания подачи заявок (по местному времени заказчика)
    @Column
    private LocalDateTime applicationDeadline;

    //Максимальная цена
    //Начальная цена
    //СПИСОК ЛОТОВ -> СВЕДЕНИЯ О ЦЕНЕ ДОГОВОРА -> Начальная (максимальная) цена договора 
    @Column
    private BigDecimal contractPrice;

    //Тип закупки
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Способ определения поставщика (подрядчика, исполнителя)
    //ОБЩИЕ СВЕДЕНИЯ О ЗАКУПКЕ -> Способ размещения закупки
    @Column
    private ProcedureType procedureType;

    //Кто опубликовал извещение о закупке
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Размещение осуществляет
    //ЗАКАЗЧИК -> Наименование организации
    @Column
    private String publisherName;

    //Есть ли ограничения по СМП
    //ПРЕИМУЩЕСТВА, ТРЕБОВАНИЯ К УЧАСТНИКАМ -> Ограничения и запреты
    //ТРЕБОВАНИЯ К УЧАСТНИКАМ ЗАКУПКИ или ПОРЯДОК ПРОВЕДЕНИЯ ПРОЦЕДУРЫ -> Порядок подведения итогов
    @Column
    private String restrictions;

    //Ссылка на закупку на площадке размещения
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Адрес электронной площадки в информационно-телекоммуникационной сети "Интернет"
    //ОБЩИЕ СВЕДЕНИЯ О ЗАКУПКЕ -> Адрес электронной площадки в информационно-телекоммуникационной сети "Интернет"
    // TODO @URL(regexp = "^(http|ftp).*")
    @Column
    private URL linkOnPlacement;

    //Обеспечение заявки
    //ОБЕСПЕЧЕНИЕ ЗАЯВКИ -> Обеспечение заявки (размер)
    // = см. документацию
    @Column
    private String applicationSecure;

    //Обеспечение контракта
    //Обеспечение исполнения контракта (размер)
    // = см. документацию
    @Column
    private String contractSecure;

    //Объект закупки
    //ОБЩАЯ ИНФОРМАЦИЯ О ЗАКУПКЕ -> Наименование объекта закупки
    //ОБЩИЕ СВЕДЕНИЯ О ЗАКУПКЕ -> Наименование закупки
    @Column
    private String objectOf;
    
    //Поле нужно чтобы отслеживать необходимость обновить данные
    @Column
    private LocalDateTime dateTimeLastUpdated;

	@Override
	public String toString() {
		return "Procurement [id=" + id + ", stage=" + stage + ", uin=" + uin + ", fzNumber=" + fzNumber
				+ ", applicationDeadline=" + applicationDeadline + ", contractPrice=" + contractPrice
				+ ", procedureType=" + procedureType + ", publisherName=" + publisherName + ", restrictions="
				+ restrictions + ", linkOnPlacement=" + linkOnPlacement + ", applicationSecure=" + applicationSecure
				+ ", contractSecure=" + contractSecure + ", objectOf=" + objectOf + ", dateTimeLastUpdated="
				+ dateTimeLastUpdated + "]";
	}
}
