package ru.zhelper.zhelper.parsers;

import ru.zhelper.zhelper.models.ProcedureType;
import ru.zhelper.zhelper.models.Procurement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.zhelper.zhelper.models.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HtmlParser44fz {

    final String URLtoProcurement = "https://zakupki.gov.ru/epz/order/notice/ea44/view/common-info.html?regNumber=";//0320300031121000109

    public HtmlParser44fz() {}

    //Procurement parseProcurementInfo(int procurementNumber) {
    public void parseProcurementInfo(String procurementNumber) {
        Procurement procurement = new Procurement();

        String stage = null;
        String uin = null;
        int fzNumber = 0;
        BigDecimal contractPrice = null;
        String procedureType = null;
        String publisherName = null;
        URL linkOnPlacement = null;
        String objectOf = null;
        LocalDateTime expirationDateSubmissionOfApplications = null;
        String applicationSecure = null;
        String contractSecure = null;

        Document htmlFile = null;

        try {
            htmlFile = Jsoup.connect(URLtoProcurement + procurementNumber).get();
        } catch (IOException e) {
            System.out.println("io - "+e);
        }
        if(htmlFile != null) {
            stage = getStage(htmlFile);

            uin = procurementNumber;

            fzNumber = 44;

            contractPrice = getPrice(htmlFile);

            procedureType = getProcedureType(htmlFile);

            Elements elements = htmlFile.getElementsByClass("blockInfo__section");
            if (elements.size() > 0) {
                for (Element element : elements) {
                    if(element.getElementsByClass("section__title").html().equals("Организация, осуществляющая размещение")) {
                        publisherName = element.getElementsByClass("section__info").html();
                    } else if(element.getElementsByClass("section__title").html().equals("Адрес электронной площадки в информационно-телекоммуникационной сети \"Интернет\"")) {
                        String linkOnPlacementTemp = element.getElementsByClass("section__info").select("a").first().html();
                        if (!linkOnPlacementTemp.isEmpty()) {
                            try {
                                linkOnPlacement = new URL(linkOnPlacementTemp);
                            } catch (MalformedURLException e) {
                                System.out.println("murle - "+e);
                                //e.printStackTrace();
                            }
                        }
                    } else if(element.getElementsByClass("section__title").html().contains("Дата и время окончания срока подачи заявок")) {
                        String datetime = element.getElementsByClass("section__info").html().substring(0,16);
                        expirationDateSubmissionOfApplications = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("d.MM.yyyy HH:mm"));
                    } else if(element.getElementsByClass("section__title").html().equals("Наименование объекта закупки")) {
                        objectOf = element.getElementsByClass("section__info").html();
                    } else if(element.getElementsByClass("section__title").html().equals("Размер обеспечения заявки")) {
                        applicationSecure = element.getElementsByClass("section__info").html().replaceAll("&nbsp;","").trim();
                    } else if(element.getElementsByClass("section__title").html().equals("Размер обеспечения исполнения контракта")) {
                        contractSecure = element.getElementsByClass("section__info").html().replaceAll("&nbsp;","").trim();
                    }
                }
            }

            String restrictions = getRestrictions(htmlFile);

            //System.out.println(stage);
            /*return new Procurement(uin, fzNumber, stage,
                    expirationDateSubmissionOfApplications, linkOnPlacement, contractPrice,
                    applicationSecure, contractSecure, procedureType, publisherName, restrictions, objectOf);*/
        }
    }

    String getStage(Document htmlFile) {
        return htmlFile.getElementsByClass("cardMainInfo__state").html();
    }

    BigDecimal getPrice(Document htmlFile) {
        String temp = null;
        BigDecimal bd = null;
        for (Element pric : htmlFile.getElementsByClass("price")) {
            temp = pric.getElementsByClass("cardMainInfo__content").addClass("cost").html().replaceAll("&nbsp;","").trim();
        }
        if(!temp.isEmpty()) {
            temp = temp.substring(0, temp.length() - 2).replace(",",".");
            bd = new BigDecimal(temp);
        }

        return bd;
    }

    String getProcedureType(Document htmlFile) {
        return htmlFile.getElementsByClass("ml-1").html();
    }

    String getRestrictions(Document htmlFile) {
        String restrictions = htmlFile.getElementsByClass("requirements_participants_block").html();
        if(!restrictions.isEmpty()) {
            restrictions = restrictions.replaceAll("&nbsp;","").replaceAll("<br>","").trim();
        }
        return restrictions;
    }
}
