package com.techthrivecatalyst.leaderboard.score;

import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends CassandraRepository<ScoreRecord, UUID> {
}
