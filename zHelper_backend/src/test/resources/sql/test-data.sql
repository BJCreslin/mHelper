CREATE TABLE IF NOT EXISTS procurements (
    id                     SERIAL PRIMARY KEY,
    stage                  VARCHAR(50),
    uin                    VARCHAR(30),
    fz_number              INT,
    application_deadline   TIMESTAMP(0),
    contract_price         NUMERIC(19, 2),
    procedure_type         VARCHAR(50),
    publisher_name         VARCHAR(500),
    restrictions           VARCHAR(4000),
    link_on_placement      VARCHAR(254),
    application_secure     VARCHAR(50),
    contract_secure        VARCHAR(50),
    status                 VARCHAR(50),
    object_of              VARCHAR(10000),
    last_updated_from_eis  TIMESTAMP(0),
    date_time_last_updated TIMESTAMP(0),
    date_of_placement      TIMESTAMP(0),
    date_of_auction        TIMESTAMP(0),
    created                TIMESTAMP(0),
    updated                TIMESTAMP(0)
);
INSERT INTO procurements (APPLICATION_DEADLINE,APPLICATION_SECURE, CONTRACT_PRICE, CONTRACT_SECURE,
                         DATE_TIME_LAST_UPDATED,FZ_NUMBER,LINK_ON_PLACEMENT,
                         OBJECT_OF,PROCEDURE_TYPE,PUBLISHER_NAME,RESTRICTIONS,
                         DATE_OF_PLACEMENT, STAGE, UIN)
                         VALUES ('1999-01-08 04:05:06','125',100000,'250',
                                '2021-01-08',615,'https://zakupki.gov.ru/epz/order/notice/ea615/view/common-info.html?regNumber=202320000012100320',
                                'Выполнение работ по капитальному ремонту общего.','ELECTRONIC_AUCTION_615FZ','НЕКОММЕРЧЕСКАЯ ОРГАНИЗАЦИЯ "ФОНД КАПИТАЛЬНОГО РЕМОНТА МНОГОКВАРТИРНЫХ ДОМОВ АМУРСКОЙ ОБЛАСТИ"','None',
                                '2021-01-14','SUBMISSION_OF_APPLICATION','202320000012100320');

INSERT INTO procurements (APPLICATION_DEADLINE,APPLICATION_SECURE, CONTRACT_PRICE, CONTRACT_SECURE,
                         DATE_TIME_LAST_UPDATED,FZ_NUMBER,LINK_ON_PLACEMENT,
                         OBJECT_OF,PROCEDURE_TYPE,PUBLISHER_NAME,RESTRICTIONS,
                         DATE_OF_PLACEMENT, STAGE, UIN)
					     VALUES ('1999-01-08 04:05:06','125',100000,'250',
					            '2021-01-08',44,'http://www.rts-tender.ru',
					            'Выполнение работ по капитальному ремонту','OPEN_COMPETITION','НЕКОММЕРЧЕСКАЯ ОРГАНИЗАЦИЯ "ФОНД КАПИТАЛЬНОГО РЕМОНТА МНОГОКВАРТИРНЫХ ДОМОВ АМУРСКОЙ ОБЛАСТИ"','None',
					            '2021-02-08','SUBMISSION_OF_APPLICATION','202320000012100322');
