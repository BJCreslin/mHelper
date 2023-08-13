package ru.mhelper.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public sealed class AbstractResponse permits JwtResponse, MessageResponse {

    protected String text;

}
