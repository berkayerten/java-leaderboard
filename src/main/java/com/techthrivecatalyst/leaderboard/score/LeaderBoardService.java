package com.techthrivecatalyst.leaderboard.score;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class LeaderBoardService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ScoreRepository scoreRepository;
    private final ChannelTopic topic;
    private ObjectMapper mapper = new ObjectMapper();

    @Value("${leaderboard.redis.sorted-set-key}")
    private String zSetKey;

    public LeaderBoardService(RedisTemplate<String, Object> redisTemplate, ScoreRepository scoreRepository,
            ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.scoreRepository = scoreRepository;
        this.topic = topic;
    }

    public void submitScore(String userId, long score) {
        persist(userId, score);
        cache(userId, score);
        publishUpdate(userId, score);
    }

    public List<Map<String, Object>> topN(int n) {
        ZSetOperations<String, Object> zops = redisTemplate.opsForZSet();
        Set<Object> raw = zops.reverseRange(zSetKey, 0, n - 1);
        if (raw == null) {
            return Collections.emptyList();
        }

        return raw.stream()
                .map(member -> createMember(member, zops))
                .toList();
    }

    private Map<String, Object> createMember(Object member, ZSetOperations<String, Object> zops) {
        Double score = zops.score(zSetKey, member);
        Map<String, Object> entry = new HashMap<>();
        entry.put("userId", member.toString());
        entry.put("score", score == null ? 0 : score.longValue());
        return entry;
    }

    private void persist(String userId, long score) {
        ScoreRecord record = new ScoreRecord(userId, score);
        scoreRepository.save(record);
    }

    private void cache(String userId, long score) {
        ZSetOperations<String, Object> zops = redisTemplate.opsForZSet();
        Double current = zops.score(zSetKey, userId);
        if (current == null || score > current) {
            zops.add(zSetKey, userId, score);
        }
    }

    private void publishUpdate(String userId, long score) {
        Map<String, Object> message = new HashMap<>();
        message.put("userId", userId);
        message.put("score", score);
        message.put("ts", System.currentTimeMillis());
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
