package ru.mhelper.security.auth;


import org.springframework.http.ResponseEntity;
import ru.mhelper.models.dto.AbstractResponse;
import ru.mhelper.models.dto.LoginRequest;
import ru.mhelper.models.dto.SignUpRequest;

/**
 * Сервис регистрации и входа
 */
public interface AuthService {

    ResponseEntity<AbstractResponse> getResponseEntity(Integer code);

    ResponseEntity<AbstractResponse> doSignIn(LoginRequest loginRequest);

    ResponseEntity<AbstractResponse> doSignUp(SignUpRequest signupRequest);

}
