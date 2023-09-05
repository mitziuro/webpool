package edu.upb.webpool.repository;

import edu.upb.webpool.domain.PoolEntry;
import edu.upb.webpool.domain.WebRtc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the PoolEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WebRtcRepository extends MongoRepository<WebRtc, String> {
    List<WebRtc> findByPoolAndOwner(String pool, String owner);

    List<WebRtc> findByPool(String pool);
}
