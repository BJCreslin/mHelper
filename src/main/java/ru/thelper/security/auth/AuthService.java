package ru.thelper.security.auth;


import org.springframework.http.ResponseEntity;
import ru.thelper.models.dto.AbstractResponse;
import ru.thelper.models.dto.LoginRequest;
import ru.thelper.models.dto.SignUpRequest;

/**
 * Сервис регистрации и входа
 */
public interface AuthService {

    ResponseEntity<AbstractResponse> getResponseEntity(Integer code);

    ResponseEntity<AbstractResponse> doSignIn(LoginRequest loginRequest);

    ResponseEntity<AbstractResponse> doSignUp(SignUpRequest signupRequest);

}
