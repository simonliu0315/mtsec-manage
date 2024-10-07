package com.cht.network.monitoring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@EnableKafka
public class SseService {

    private static final Logger log = LoggerFactory.getLogger(SseService.class);

    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @KafkaListener(topics = "notification-topic")
    public void listen(String message) {
        sseEmitters.values().forEach(emitter -> {
            try {
                emitter.send(message);
            } catch (Exception e) {
                // 處理異常，例如移除失效的SseEmitter
                sseEmitters.remove(emitter);
            }
        });
    }

    public SseEmitter subscribe() {
        SseEmitter sseEmitter = new SseEmitter();
        // ... (設置超时处理等)
        sseEmitter.onCompletion(() -> {
            synchronized (this.sseEmitters) {
                this.sseEmitters.remove(sseEmitter);
            }
        });

        sseEmitter.onTimeout(()-> {
            sseEmitter.complete();
        });
        sseEmitters.put(UUID.randomUUID().toString(), sseEmitter);

        return sseEmitter;
    }
}
