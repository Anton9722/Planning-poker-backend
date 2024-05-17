package com.PlanningPoker.PlanningPoker.service;

import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.core.MongoOperations;

import com.PlanningPoker.PlanningPoker.models.Issue;

public class IssueService {
    private MongoOperations mongoOperations;

    public IssueService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public List<Issue> getProjectIssues(UUID projectId) {
        return null;
    }

    public Issue getIssueById(UUID issueId) {
        return null;
    }

    public Issue createIssue(Issue issue) {
        return null;
    }

    public Issue deleteIssue(UUID issueId) {
        return null;
    }

    public Issue estimateTime(UUID issueId) {
        return null;
    }

    public Issue closeIssue(UUID issueId) {
        return null;
    }
}
