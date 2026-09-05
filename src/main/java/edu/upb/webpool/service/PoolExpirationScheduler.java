package edu.upb.webpool.service;

import edu.upb.webpool.domain.Pool;
import edu.upb.webpool.repository.PoolRepository;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PoolExpirationScheduler {

    private static final Logger log = LoggerFactory.getLogger(PoolExpirationScheduler.class);

    private final PoolRepository poolRepository;
    private final PoolResultsEmailService resultsEmailService;

    public PoolExpirationScheduler(PoolRepository poolRepository, PoolResultsEmailService resultsEmailService) {
        this.poolRepository = poolRepository;
        this.resultsEmailService = resultsEmailService;
    }

    @Scheduled(fixedDelayString = "${application.results-email-check-ms:60000}", initialDelayString = "${application.results-email-initial-delay-ms:30000}")
    public void emailExpiredPollResults() {
        Instant now = Instant.now();
        poolRepository
            .findAll()
            .stream()
            .filter(pool -> pool.getEndDate() != null)
            .filter(pool -> !pool.getEndDate().isAfter(now))
            .filter(pool -> pool.getResultsSentAt() == null)
            .forEach(this::sendSafely);
    }

    private void sendSafely(Pool pool) {
        try {
            resultsEmailService.sendResults(pool);
        } catch (RuntimeException exception) {
            log.warn("Automatic result email failed for poll {}", pool.getId(), exception);
        }
    }
}
