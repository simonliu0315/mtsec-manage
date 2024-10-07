package com.cht.network.monitoring.web.rest;

import com.cht.network.monitoring.service.SseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@EnableKafka
@RequestMapping("api/NotificationSender")
public class NotificationSenderResource {

    private static final Logger log = LoggerFactory.getLogger(NotificationSenderResource.class);

    private final SseService sseService;

    public NotificationSenderResource(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sendEvents() {
        return sseService.subscribe();
    }
}
