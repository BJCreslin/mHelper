package ru.mhelper.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public non-sealed class JwtResponse extends AbstractResponse {

    private String accessToken;

    private String refreshToken;

    @JsonIgnore
    private String type;

    @JsonIgnore
    private Long id;

    private String userName;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private Collection<String> roles;

}
