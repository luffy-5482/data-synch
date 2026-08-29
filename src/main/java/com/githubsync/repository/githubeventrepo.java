package com.githubsync.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.githubsync.dto.githubevent;

public interface  githubeventrepo extends JpaRepository<githubevent, Long>{
    
}
