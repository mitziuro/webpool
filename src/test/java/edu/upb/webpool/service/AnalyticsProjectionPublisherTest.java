package edu.upb.webpool.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.upb.webpool.client.AnalyticsProjectionClient;
import edu.upb.webpool.client.dto.AnalyticsPoolProjection;
import edu.upb.webpool.client.dto.AnalyticsSnapshot;
import edu.upb.webpool.client.dto.AnalyticsVoteProjection;
import edu.upb.webpool.domain.Pool;
import edu.upb.webpool.domain.PoolEntry;
import edu.upb.webpool.repository.PoolEntryRepository;
import edu.upb.webpool.repository.PoolRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class AnalyticsProjectionPublisherTest {

    private final AnalyticsProjectionClient client = mock(AnalyticsProjectionClient.class);
    private final PoolRepository poolRepository = mock(PoolRepository.class);
    private final PoolEntryRepository entryRepository = mock(PoolEntryRepository.class);
    private final AnalyticsProjectionPublisher publisher = new AnalyticsProjectionPublisher(
        client,
        poolRepository,
        entryRepository,
        "test-key"
    );

    @Test
    void publishesOnlyTheFieldsRequiredByAnalytics() {
        PoolEntry vote = new PoolEntry()
            .id("vote-1")
            .pool("poll-1")
            .owner("ana@example.com")
            .optionValue("Pizza")
            .date(Instant.parse("2026-09-06T08:00:00Z"));

        publisher.publishVote(vote);

        ArgumentCaptor<AnalyticsVoteProjection> captor = ArgumentCaptor.forClass(AnalyticsVoteProjection.class);
        verify(client).upsertVote(eq("test-key"), captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo("vote-1");
        assertThat(captor.getValue().getPool()).isEqualTo("poll-1");
        assertThat(captor.getValue().getOwner()).isEqualTo("ana@example.com");
    }

    @Test
    void sendsACompleteSnapshotForRecovery() {
        when(poolRepository.findAll()).thenReturn(List.of(new Pool().id("poll-1").name("Planning")));
        when(entryRepository.findAll()).thenReturn(List.of(new PoolEntry().id("vote-1").pool("poll-1")));

        publisher.synchronizeAll();

        ArgumentCaptor<AnalyticsSnapshot> captor = ArgumentCaptor.forClass(AnalyticsSnapshot.class);
        verify(client).synchronize(eq("test-key"), captor.capture());
        List<AnalyticsPoolProjection> pools = captor.getValue().getPools();
        assertThat(pools).extracting(AnalyticsPoolProjection::getId).containsExactly("poll-1");
        assertThat(captor.getValue().getVotes()).extracting(AnalyticsVoteProjection::getId).containsExactly("vote-1");
    }
}
