package com.githubsync.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.githubsync.service.githubwebhookservice;

@RestController
@RequestMapping("webhook")
public class githubsynccontroller { 
    
    // 1. Made final for good practice (immutable dependency injection)
    private final githubwebhookservice githubwebhookservice;

    // 2. Fixed the parameter name to perfectly match the assignment logic
    public githubsynccontroller(githubwebhookservice githubwebhookservice) {
        this.githubwebhookservice = githubwebhookservice;
    }

    @PostMapping("/github")
    public ResponseEntity<String> receiveWebhook(
            @RequestHeader(value = "X-GitHub-Event", required = false) String event,
            @RequestHeader(value = "X-GitHub-Delivery", required = false) String deliveryId,
            @RequestBody String payload) {

        githubwebhookservice.processWebhook(
                event,
                deliveryId,
                payload
        );
        return ResponseEntity.ok("Webhook received");
    }
}
