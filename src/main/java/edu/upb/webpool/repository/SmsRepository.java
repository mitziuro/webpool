package edu.upb.webpool.repository;

import edu.upb.webpool.domain.Sms;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the PoolEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsRepository extends CassandraRepository<Sms, String> {
    @AllowFiltering
    List<Sms> findByPoolAndOwner(String pool, String owner);

    @AllowFiltering
    List<Sms> findByPool(String pool);

    @AllowFiltering
    void deleteByPoolAndOwner(String pool, String owner);

}
