package com.githubsync.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.githubsync.dto.githubevent;
import com.githubsync.repository.githubeventrepo;

@Service
public class githubwebhookservice {
  private final githubeventrepo githubEventRepository;

    public githubwebhookservice(githubeventrepo githubEventRepository) {
        this.githubEventRepository = githubEventRepository;
    }

    public void processEvent(
            String eventType,
            String deliveryId,
            String payload) {

        githubevent event = new githubevent();

        event.setEventType(eventType);
        event.setDeliveryId(deliveryId);
        event.setPayload(payload);
        event.setReceivedAt(LocalDateTime.now());
        event.setProcessed(false);

        githubEventRepository.save(event);

        System.out.println("GitHub event saved: " + eventType);
    }
}