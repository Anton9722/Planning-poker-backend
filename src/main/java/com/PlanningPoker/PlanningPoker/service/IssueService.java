package com.PlanningPoker.PlanningPoker.service;

import java.util.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.http.*;
import com.PlanningPoker.PlanningPoker.models.*;

public class IssueService {
    private final MongoOperations mongoOperations;

    public IssueService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public ResponseEntity<?> getProjectIssues(String projectId, String sessionId) {
        return null;
    }

    public ResponseEntity<?> getIssueById(String issueId, String sessionId) {
        return mongoOperations.find(new Query(Criteria.where("issueId").is(issueId)), Issue.class) != null
                ? ResponseEntity.ok().body(issueId)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
    }

    public ResponseEntity<?> createIssue(Issue issue, String sessionId) {
        return mongoOperations.insert(issue) != null
                ? ResponseEntity.ok().body(issue)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
    }

    public ResponseEntity<?> deleteIssue(String issueId, String sessionId) {
        return mongoOperations.remove(new Query(Criteria.where("issueId").is(issueId)), Issue.class).wasAcknowledged()
                ? ResponseEntity.ok().body(issueId)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
    }

    public ResponseEntity<?> assignIssue(String userId, String issueId, String sessionId) {
        return null;
    }

    public ResponseEntity<?> estimateTime(String userId, String issueId, int estimatedTime, String sessionId) {
        return null;
    }

    // Avslutar ett issue. Sätter isDone till true.
    public ResponseEntity<?> closeIssue(String issueId, String sessionId) {
        return mongoOperations
                .updateFirst(new Query(Criteria.where("issueId").is(issueId)), Update.update("isDone", true),
                        Issue.class)
                .wasAcknowledged()
                        ? ResponseEntity.ok().body(issueId)
                        : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
    }
}
