package edu.upb.webpool.repository;

import edu.upb.webpool.domain.PoolEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PoolEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoolEntryRepository extends MongoRepository<PoolEntry, String> {}
