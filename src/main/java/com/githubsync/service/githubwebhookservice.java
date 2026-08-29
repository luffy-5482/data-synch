package com.githubsync.service;

import org.springframework.stereotype.Service;

@Service
public class githubwebhookservice {

    public void processEvent(
            String event,
            String deliveryId,
            String payload) {

        System.out.println("=================================");
        System.out.println("GitHub Event Received");
        System.out.println("Event       : " + event);
        System.out.println("Delivery ID : " + deliveryId);
        System.out.println("Payload     : ");
        System.out.println(payload);
        System.out.println("=================================");
    }
}