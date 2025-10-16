package com.techthrivecatalyst.leaderboard.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageSubscriber implements MessageListener {

    private static final String WS_SUBSCRIBER = "/topic/leaderboard";
    private final SimpMessagingTemplate simp;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChannelTopic topic;

    public RedisMessageSubscriber(SimpMessagingTemplate simp, ChannelTopic topic) {
        this.simp = simp;
        this.topic = topic;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = message.toString();
        try {
            Map<?, ?> map = objectMapper.readValue(payload, Map.class);
            simp.convertAndSend(WS_SUBSCRIBER, map);
        } catch (JsonProcessingException e) {
            System.out.println("Error parsing payload: " + e.getMessage());
        }
    }
}
