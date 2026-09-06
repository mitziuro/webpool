package edu.upb.webpool.service;

import edu.upb.webpool.client.AnalyticsProjectionClient;
import edu.upb.webpool.client.dto.AnalyticsPoolProjection;
import edu.upb.webpool.client.dto.AnalyticsSnapshot;
import edu.upb.webpool.client.dto.AnalyticsVoteProjection;
import edu.upb.webpool.domain.Pool;
import edu.upb.webpool.domain.PoolEntry;
import edu.upb.webpool.repository.PoolEntryRepository;
import edu.upb.webpool.repository.PoolRepository;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsProjectionPublisher {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsProjectionPublisher.class);

    private final AnalyticsProjectionClient client;
    private final PoolRepository poolRepository;
    private final PoolEntryRepository poolEntryRepository;
    private final String serviceKey;

    public AnalyticsProjectionPublisher(
        AnalyticsProjectionClient client,
        PoolRepository poolRepository,
        PoolEntryRepository poolEntryRepository,
        @Value("${webpool.analytics.service-key}") String serviceKey
    ) {
        this.client = client;
        this.poolRepository = poolRepository;
        this.poolEntryRepository = poolEntryRepository;
        this.serviceKey = serviceKey;
    }

    public void publishPool(Pool pool) {
        safely("publish poll " + pool.getId(), () -> client.upsertPool(serviceKey, toProjection(pool)));
    }

    public void publishVote(PoolEntry vote) {
        safely("publish vote " + vote.getId(), () -> client.upsertVote(serviceKey, toProjection(vote)));
    }

    public void deletePool(String id) {
        safely("delete poll projection " + id, () -> client.deletePool(serviceKey, id));
    }

    public void deleteVote(String id) {
        safely("delete vote projection " + id, () -> client.deleteVote(serviceKey, id));
    }

    public void synchronizeAll() {
        AnalyticsSnapshot snapshot = new AnalyticsSnapshot(
            poolRepository.findAll().stream().map(this::toProjection).collect(Collectors.toList()),
            poolEntryRepository.findAll().stream().map(this::toProjection).collect(Collectors.toList())
        );
        safely("synchronize analytics projection", () -> client.synchronize(serviceKey, snapshot));
    }

    AnalyticsPoolProjection toProjection(Pool pool) {
        AnalyticsPoolProjection projection = new AnalyticsPoolProjection();
        projection.setId(pool.getId());
        projection.setName(pool.getName());
        projection.setEndDate(pool.getEndDate());
        projection.setOwner(pool.getOwner());
        projection.setType(pool.getType());
        projection.setFinalValue(pool.getFinal());
        return projection;
    }

    AnalyticsVoteProjection toProjection(PoolEntry vote) {
        AnalyticsVoteProjection projection = new AnalyticsVoteProjection();
        projection.setId(vote.getId());
        projection.setPool(vote.getPool());
        projection.setOptionValue(vote.getOptionValue());
        projection.setOption(vote.getOption());
        projection.setDate(vote.getDate());
        projection.setOwner(vote.getOwner());
        projection.setType(vote.getType());
        projection.setVideoAttached(vote.getVideoAttached());
        return projection;
    }

    private void safely(String operation, Runnable action) {
        try {
            action.run();
        } catch (RuntimeException exception) {
            // Analytics is an eventually consistent read model. A temporary
            // outage must never roll back or reject a valid vote.
            log.warn("Could not {}; the scheduled reconciliation will retry", operation, exception);
        }
    }
}
