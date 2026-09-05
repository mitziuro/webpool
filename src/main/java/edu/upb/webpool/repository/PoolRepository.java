package edu.upb.webpool.repository;

import edu.upb.webpool.domain.Pool;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Pool entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoolRepository extends CassandraRepository<Pool, String> {

    @AllowFiltering
    List<Pool> findByOwner(String owner);
}
