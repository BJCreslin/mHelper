package ru.mhelper.services.daily_starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.mhelper.services.dao.ProcurementDataManager;

@Service
public class IntervalStarterImpl implements IntervalStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntervalStarterImpl.class);

    private final ProcurementDataManager dataManager;

    public IntervalStarterImpl(ProcurementDataManager dataManager) {
        this.dataManager = dataManager;
    }

    //@Scheduled(initialDelayString = "${scheduler.delay}", fixedDelayString = "${scheduler.interval_between_launches}")
    @Async
    @Override
    public void doWork() throws InterruptedException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Start daily scheduling- delete old procurements.");
        }
        var procurements = dataManager.loadAll();

    }
}
