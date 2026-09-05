package edu.upb.webpool.repository;

import edu.upb.webpool.domain.User;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends CassandraRepository<User, String> {

    @Query("SELECT * FROM users WHERE email = ?0 ALLOW FILTERING")
    List<User> findByEmailIgnoreCase(String email);

}
