package edu.upb.webpool.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsProjectionSynchronizer {

    private final AnalyticsProjectionPublisher publisher;

    public AnalyticsProjectionSynchronizer(AnalyticsProjectionPublisher publisher) {
        this.publisher = publisher;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void synchronizeOnStartup() {
        publisher.synchronizeAll();
    }

    @Scheduled(
        fixedDelayString = "${application.analytics-sync-fixed-delay-ms:300000}",
        initialDelayString = "${application.analytics-sync-initial-delay-ms:30000}"
    )
    public void reconcilePeriodically() {
        publisher.synchronizeAll();
    }
}
