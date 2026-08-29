package com.githubsync.service;

import org.springframework.stereotype.Service;

import com.githubsync.dto.githubcommit;
import com.githubsync.dto.githubevent;
import com.githubsync.dto.githubpusheddata;
import com.githubsync.repository.githubeventrepo;

import tools.jackson.databind.ObjectMapper;

@Service
public class githubwebhookservice {
 
    private final githubeventrepo githubEventRepo;
    private final ObjectMapper objectMapper;

    public githubwebhookservice(
            githubeventrepo githubEventRepo,
            ObjectMapper objectMapper) {

        this.githubEventRepo = githubEventRepo;
        this.objectMapper = objectMapper;
    }

    public void processWebhook(
            String eventType,
            String deliveryId,
            String payload) {

        try {

            // Convert JSON → Java object

            githubpusheddata githubData =
                    objectMapper.readValue(
                            payload,
                            githubpusheddata.class
                    );

            // Repository

            System.out.println(
                    "Repository: "
                    + githubData.getRepository().getFull_name()
            );

            // Pusher

            System.out.println(
                    "Pusher: "
                    + githubData.getPusher().getName()
            );

            System.out.println(
                    "Pusher Email: "
                    + githubData.getPusher().getEmail()
            );

            // Branch

            System.out.println(
                    "Branch: "
                    + githubData.getRef()
            );

            // Commits

            for (githubcommit commit : githubData.getCommits()) {

                System.out.println(
                        "Commit ID: "
                        + commit.getId()
                );

                System.out.println(
                        "Commit Message: "
                        + commit.getMessage()
                );
            }

            // Save original webhook

            githubevent githubEvent = new githubevent();

            githubEvent.setDeliveryId(deliveryId);
            githubEvent.setEventType(eventType);
            githubEvent.setPayload(payload);
            githubEvent.setProcessed(false);

            githubEventRepo.save(githubEvent);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to process GitHub webhook",
                    e
            );
        }
    }
}