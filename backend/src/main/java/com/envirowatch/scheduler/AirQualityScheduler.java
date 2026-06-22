package com.envirowatch.scheduler;

import com.envirowatch.service.AirQualitySyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class AirQualityScheduler {

    private static final Logger log = LoggerFactory.getLogger(AirQualityScheduler.class);

    private final AirQualitySyncService syncService;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public AirQualityScheduler(AirQualitySyncService syncService) {
        this.syncService = syncService;
    }

    /**
     * Runs every hour. The AtomicBoolean guard prevents overlapping executions
     * if a previous sync is still running.
     */
    @Scheduled(fixedRateString = "${openaq.sync-interval-ms:3600000}", initialDelay = 10000)
    public void scheduledSync() {
        if (!running.compareAndSet(false, true)) {
            log.warn("Previous sync is still running. Skipping this run.");
            return;
        }
        try {
            syncService.syncAll();
        } finally {
            running.set(false);
        }
    }
}
