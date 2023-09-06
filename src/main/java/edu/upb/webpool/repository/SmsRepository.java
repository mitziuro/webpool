package edu.upb.webpool.repository;

import edu.upb.webpool.domain.Sms;
import edu.upb.webpool.domain.WebRtc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the PoolEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsRepository extends MongoRepository<Sms, String> {
    List<Sms> findByPoolAndOwner(String pool, String owner);

    List<Sms> findByPool(String pool);

    void deleteByPoolAndOwner(String pool, String owner);

}
