package ru.mhelper.models.dto;

import lombok.Data;

@Data
public sealed class AbstractResponse permits JwtResponse, MessageResponse {

    private String text;

}
