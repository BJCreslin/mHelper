package ru.thelper.cfg;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Кнфигурцаия для создания метрик для мониторинга prometheus
 */
@Configuration
public class RegistryConfig {

    /**
     * Название метрики - Колочество обращений
     */
    public static final String API_NEW_PROCUREMENT_COUNT = "api_new_procurement_count";

    @Bean
    MeterBinder meterBinder() {
        return meterRegistry ->
                Counter.builder(API_NEW_PROCUREMENT_COUNT)
                        .description("Количество обращений")
                        .register(meterRegistry);
    }
}
