package ru.mhelper.services.metrics;

/**
 * Сервис для работы с метриками
 */
public interface MetricsService {

    /**
     * Increments the API count for new procurements.
     * This function is responsible for incrementing the API count metric
     * whenever a new procurement is created through the API.
     */
    void incrementApiNewProcurements();
}
