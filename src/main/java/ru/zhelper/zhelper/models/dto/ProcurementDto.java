package ru.zhelper.zhelper.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ProcurementDto {
    private String fzNumber;
    private String uin;
    private String objectOf;
    private String publisherName;
    private String contractPrice;
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
}
