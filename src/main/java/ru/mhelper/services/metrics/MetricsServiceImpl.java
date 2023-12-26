package ru.mhelper.services.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mhelper.cfg.RegistryConfig;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    private final MeterRegistry meterRegistry;

    @Override
    public void incrementApiNewProcurements() {
        meterRegistry.counter(RegistryConfig.API_NEW_PROCUREMENT_COUNT, List.of()).increment();
    }
}
