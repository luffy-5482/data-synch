package com.githubsync.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.githubsync.entity.user;

public interface userrepo extends JpaRepository<user, Long> {

    Optional<user> findByGithubUsername(String githubUsername);
}