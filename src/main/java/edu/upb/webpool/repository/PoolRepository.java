package edu.upb.webpool.repository;

import edu.upb.webpool.domain.Pool;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Pool entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoolRepository extends MongoRepository<Pool, String> {

    List<Pool> findByOwner(String owner);

    List<Pool> findByFinalValueIsNotNullAndUsers(List<String> users);
    List<Pool> findByFinalValueIsNullAndUsers(List<String> users);

    List<Pool> findByFinalValueIsNullAndOwnerAndVote(String owner, boolean vote);
    List<Pool> findByFinalValueIsNotNullAndOwnerAndVote(String owner, boolean vote);


}
