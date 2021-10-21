package ru.zhelper.zhelper.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ProcurementDto {
    @NotNull
    private String fzNumber;
    @NotNull
    private String uin;
    @NotNull
    private String objectOf;
    @NotNull
    private String publisherName;
    private String contractPrice;
    @NotNull
    private String procedureType;
    private String stage;
    private String linkOnPlacement;
    private String applicationDeadline;
    private String applicationSecure;
    private String contractSecure;
    private String restrictions;
    private String lastUpdatedFromEIS;
    private String dateOfPlacement;
    private String dateOfAuction;
    private String timeOfAuction;
    private String timeZone;
    private String etpName;
    private String etpUrl;
    private String summingUpDate;
}
