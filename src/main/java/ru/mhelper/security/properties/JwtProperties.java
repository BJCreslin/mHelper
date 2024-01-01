package ru.mhelper.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * класс для хранения свойст токенов
 */
@Component
@Data
@ConfigurationProperties(prefix = "jwt.token")
public class JwtProperties {

    private long expired;

    private long refresh;

    private String secret;

    private String header;
}
