package ru.mhelper.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Builder
@Data
public class JwtResponse extends AbstractResponse {

    private String accessToken;

    private String refreshToken;

    private String type;

    @JsonIgnore
    private Long id;

    private String userName;

    private String email;

    private Collection<String> roles;

}
