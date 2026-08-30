package com.githubsync.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class emailservice {

    private final JavaMailSender mailSender;

    public emailservice(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendGithubNotification(
            String email,
            String repository,
            String pusher,
            String commitMessage) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);

        message.setSubject(
                "GitHub Update - " + repository
        );

        message.setText(
                "A new update was pushed to your GitHub repository.\n\n"
                + "Repository: " + repository + "\n"
                + "Pusher: " + pusher + "\n"
                + "Commit: " + commitMessage
        );

        mailSender.send(message);
    }
}