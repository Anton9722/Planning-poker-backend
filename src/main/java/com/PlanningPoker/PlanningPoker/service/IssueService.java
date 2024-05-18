package com.PlanningPoker.PlanningPoker.service;

import java.util.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.PlanningPoker.PlanningPoker.models.*;

@Service
public class IssueService {
    private final MongoOperations mongoOperations;

    public IssueService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    // Returnerar alla issues från ett projekt.
    public ResponseEntity<?> getProjectIssues(String projectId, String sessionId) {
        List<Issue> issues = mongoOperations.findAll(Issue.class);
        if (issues.size() > 0) {
            return ResponseEntity.ok().body(issues.stream().filter((Issue issue) -> issue.getProjectId().equals(projectId)));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
        }
    }

    // Returnerar ett issue.
    public ResponseEntity<?> getIssueById(String issueId, String sessionId) {
        return mongoOperations.findOne(new Query(Criteria.where("issueId").is(issueId)), Issue.class) != null
            ? ResponseEntity.ok().body(issueId)
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
    }

    // Skapar ett issue.
    public ResponseEntity<?> createIssue(Issue issue, String sessionId) {
        return mongoOperations.insert(issue) != null
            ? ResponseEntity.ok().body(issue)
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
    }

    // Raderar ett issue.
    public ResponseEntity<?> deleteIssue(String issueId, String sessionId) {
        return mongoOperations.remove(new Query(Criteria.where("issueId").is(issueId)), Issue.class).wasAcknowledged()
            ? ResponseEntity.ok().body(issueId)
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
    }

    // Tilldelar en medlem till ett issue.
    public ResponseEntity<?> assignIssue(String userId, String issueId, String sessionId) {
        Issue issue = mongoOperations.findOne(new Query(Criteria.where("issueId").is(issueId)), Issue.class);
        if (issue != null) {
            issue.getAssignedIds().add(userId);
            mongoOperations.updateFirst(new Query(Criteria.where("issueId").is(issueId)), Update.update("assignedIds", issue.getAssignedIds()), Issue.class);
            return ResponseEntity.ok().body(issue);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
        }
    }

    // Sätter estimerad tid.
    public ResponseEntity<?> estimateTime(String userId, String issueId, int estimatedTime, String sessionId) {
        Issue issue = mongoOperations.findOne(new Query(Criteria.where("issueId").is(issueId)), Issue.class);
        if (issue != null) {
            issue.getEstimatedTimes().put(userId, estimatedTime);
            mongoOperations.updateFirst(new Query(Criteria.where("issueId").is(issueId)), Update.update("estimatedTimes", issue.getEstimatedTimes()), Issue.class);
            return ResponseEntity.ok().body(issue);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
        }
    }

    // Avslutar ett issue. Sätter isDone till true.
    public ResponseEntity<?> closeIssue(String issueId, String sessionId) {
        return mongoOperations
            .updateFirst(new Query(Criteria.where("issueId").is(issueId)), Update.update("isDone", true), Issue.class).wasAcknowledged()
                ? ResponseEntity.ok().body(issueId)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
    }
}
