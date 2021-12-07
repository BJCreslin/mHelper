package ru.zhelper.zhelper.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JwtResponse extends AbstractResponse{
    private String token;
    private String type = "Bearer";
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
    }
}
