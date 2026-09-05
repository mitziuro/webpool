package edu.upb.webpool.repository;

import edu.upb.webpool.domain.VoteConfirmationChallenge;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteConfirmationChallengeRepository extends CassandraRepository<VoteConfirmationChallenge, String> {}
