package com.techthrivecatalyst.leaderboard.score;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("scores")
public class ScoreRecord {

    @PrimaryKey
    private UUID id;

    @Column("user_id")
    private String userId;

    @Column("score")
    private long score;

    @Column("created_at")
    private Instant createdAt;

    public ScoreRecord() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public ScoreRecord(String userId, long score) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.score = score;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
