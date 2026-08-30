package com.githubsync.service;

import org.springframework.stereotype.Service;

import com.githubsync.dto.githubcommit;
import com.githubsync.dto.githubevent;
import com.githubsync.dto.githubpusheddata;
import com.githubsync.repository.githubeventrepo;
import com.githubsync.repository.userrepo;

import tools.jackson.databind.ObjectMapper;

@Service
public class githubwebhookservice {

    private final githubeventrepo githubEventRepo;
    private final userrepo userRepo;
    private final emailservice emailService;
    private final ObjectMapper objectMapper;

    public githubwebhookservice(
            githubeventrepo githubEventRepo,
            userrepo userRepo,
            emailservice emailService,
            ObjectMapper objectMapper) {

        this.githubEventRepo = githubEventRepo;
        this.userRepo = userRepo;
        this.emailService = emailService;
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

            // -----------------------------
            // 1. Extract repository
            // -----------------------------

            String repositoryName =
                    githubData.getRepository().getFull_name();

            System.out.println(
                    "Repository: " + repositoryName
            );


            // -----------------------------
            // 2. Extract pusher
            // -----------------------------

            String pusherName =
                    githubData.getPusher().getName();

            System.out.println(
                    "Pusher: " + pusherName
            );


            // -----------------------------
            // 3. Find registered user
            // -----------------------------

            userRepo
                    .findByGithubUsername(pusherName)
                    .ifPresentOrElse(

                            registeredUser -> {

                                System.out.println(
                                        "Registered email: "
                                        + registeredUser.getEmail()
                                );

                                // -----------------------------
                                // 4. Process commits
                                // -----------------------------

                                for (githubcommit commit
                                        : githubData.getCommits()) {

                                    System.out.println(
                                            "Commit ID: "
                                            + commit.getId()
                                    );

                                    System.out.println(
                                            "Commit Message: "
                                            + commit.getMessage()
                                    );

                                    // -----------------------------
                                    // 5. Send email
                                    // -----------------------------

                                    emailService.sendGithubNotification(
                                            registeredUser.getEmail(),
                                            repositoryName,
                                            pusherName,
                                            commit.getMessage()
                                    );
                                }
                            },

                            () -> {

                                System.out.println(
                                        "No registered user found for: "
                                        + pusherName
                                );

                            }
                    );


            // -----------------------------
            // 6. Store webhook
            // -----------------------------

            githubevent githubEvent = new githubevent();

            githubEvent.setDeliveryId(deliveryId);
            githubEvent.setEventType(eventType);
            githubEvent.setPayload(payload);
            githubEvent.setProcessed(true);

            githubEventRepo.save(githubEvent);


        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to process GitHub webhook",
                    e
            );
        }
    }
}