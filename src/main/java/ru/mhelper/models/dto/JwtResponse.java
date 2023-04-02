package ru.mhelper.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Builder
public class JwtResponse extends AbstractResponse {

    private String accessToken;

    private String refreshToken;

    private String type;

    private Long id;

    private String userName;

    private String email;

    private Collection<String> roles;

}
