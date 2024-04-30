package ru.thelper.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public sealed class AbstractResponse permits JwtResponse, MessageResponse {

    protected String text;

}
