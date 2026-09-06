package edu.upb.webpool.client;

import edu.upb.webpool.client.dto.AnalyticsPoolProjection;
import edu.upb.webpool.client.dto.AnalyticsSnapshot;
import edu.upb.webpool.client.dto.AnalyticsVoteProjection;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "webpoolanalytics", path = "/api/internal/analytics-projections")
public interface AnalyticsProjectionClient {

    @PutMapping("/pools")
    void upsertPool(@RequestHeader("X-WebPool-Service-Key") String serviceKey, @RequestBody AnalyticsPoolProjection pool);

    @DeleteMapping("/pools/{id}")
    void deletePool(@RequestHeader("X-WebPool-Service-Key") String serviceKey, @PathVariable("id") String id);

    @PutMapping("/votes")
    void upsertVote(@RequestHeader("X-WebPool-Service-Key") String serviceKey, @RequestBody AnalyticsVoteProjection vote);

    @DeleteMapping("/votes/{id}")
    void deleteVote(@RequestHeader("X-WebPool-Service-Key") String serviceKey, @PathVariable("id") String id);

    @PutMapping("/snapshot")
    void synchronize(@RequestHeader("X-WebPool-Service-Key") String serviceKey, @RequestBody AnalyticsSnapshot snapshot);
}
