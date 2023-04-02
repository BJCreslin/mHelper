package ru.mhelper.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class JwtResponse extends AbstractResponse {

    @JsonIgnore
    public static final String BEARER_NAME = "Bearer";
    private String token;

    private String type;
    private Long id;
    private String userName;
    private String email;
    private List<String> roles;

    public JwtResponse(String token, Long id, String userName, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.roles = roles;
        this.type = BEARER_NAME;
    }
}
