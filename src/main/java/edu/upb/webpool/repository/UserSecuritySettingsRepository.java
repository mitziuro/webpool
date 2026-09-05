package edu.upb.webpool.repository;

import edu.upb.webpool.domain.UserSecuritySettings;
import java.util.Optional;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSecuritySettingsRepository extends CassandraRepository<UserSecuritySettings, String> {
    @Query("SELECT * FROM users WHERE email = ?0 ALLOW FILTERING")
    Optional<UserSecuritySettings> findOneByEmail(String email);

    @Query("SELECT * FROM users WHERE login = ?0 ALLOW FILTERING")
    Optional<UserSecuritySettings> findOneByLogin(String login);
}
