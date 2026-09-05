package edu.upb.webpool.repository;

import edu.upb.webpool.domain.PoolEntry;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the PoolEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoolEntryRepository extends CassandraRepository<PoolEntry, String> {
    @AllowFiltering
    List<PoolEntry> findByPoolAndOwner(String pool, String owner);

    @AllowFiltering
    List<PoolEntry> findByPool(String pool);
}
