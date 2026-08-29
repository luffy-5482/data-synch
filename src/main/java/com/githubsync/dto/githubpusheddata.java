package com.githubsync.dto;

import java.util.List;

import lombok.Data;

@Data
public class githubpusheddata {
        private String ref;

    private githubrepository repository;

    private githubpusher pusher;

    private List<githubcommit> commits;
}
