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

    // Authenticate
    public boolean authenticateUser(String id, String sessionId) {
        if (id == null || sessionId == null || id.isEmpty() || sessionId.isEmpty()) {
            return false;
        }
        User foundUser = mongoOperations.findOne(new Query(Criteria.where("id").is(id)), User.class);
        return foundUser != null && foundUser.getSessionId() != null && foundUser.getSessionId().equals(sessionId);
    }

    // Returnerar alla issues från ett projekt.
    public ResponseEntity<?> getProjectIssues(String userId, String projectId, String sessionId) {
        if (authenticateUser(userId, sessionId) == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }
        List<Issue> issues = mongoOperations.findAll(Issue.class).stream()
                .filter((Issue issue) -> issue.getProjectId().equals(projectId)).toList();
        return !issues.isEmpty()
                ? ResponseEntity.ok().body(issues)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("No issues found.");
    }

    // Returnerar ett issue.
    public ResponseEntity<?> getIssueById(String userId, String issueId, String sessionId) {
        if (authenticateUser(userId, sessionId) == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }
        return mongoOperations.findOne(new Query(Criteria.where("id").is(issueId)), Issue.class) != null
                ? ResponseEntity.ok().body(issueId)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
    }

    // Skapar ett issue.
    public ResponseEntity<?> createIssue(String userId, Issue issue, String sessionId) {
        if (authenticateUser(userId, sessionId) == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }
        return mongoOperations.insert(issue) != null
                ? ResponseEntity.ok().body(issue)
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Issue not created.");
    }

    // Tar bort ett issue.
    public ResponseEntity<?> deleteIssue(String userId, String issueId, String sessionId) {
        if (authenticateUser(userId, sessionId) == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }
        return mongoOperations.remove(new Query(Criteria.where("id").is(issueId)), Issue.class).getDeletedCount() > 0
                ? ResponseEntity.ok().body(issueId)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
    }

    // Tilldelar en medlem till ett issue.
    public ResponseEntity<?> assignIssue(String userId, String issueId, String sessionId) {
        if (authenticateUser(userId, sessionId) == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }
        Issue issue = mongoOperations.findOne(new Query(Criteria.where("id").is(issueId)), Issue.class);
        if (issue != null) {
            issue.getAssignedIds().add(userId);
            mongoOperations.updateFirst(new Query(Criteria.where("id").is(issueId)),
                    Update.update("assignedIds", issue.getAssignedIds()), Issue.class);
            return ResponseEntity.ok().body(issue);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
        }
    }

    // Sätter estimerad tid.
    public ResponseEntity<?> assignTime(String userId, String issueId, int estimatedTime, String sessionId) {
        if (authenticateUser(userId, sessionId) == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }
        Issue issue = mongoOperations.findOne(new Query(Criteria.where("id").is(issueId)), Issue.class);
        if (issue != null) {
            issue.getEstimatedTimes().put(userId, estimatedTime);
            mongoOperations.updateFirst(new Query(Criteria.where("id").is(issueId)),
                    Update.update("estimatedTimes", issue.getEstimatedTimes()), Issue.class);
            return ResponseEntity.ok().body(issue);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
        }
    }

    // Avslutar ett issue. Sätter isDone till true.
    public ResponseEntity<?> closeIssue(String userId, String issueId, int completedTime, String sessionId) {
        if (authenticateUser(userId, sessionId) == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }
        Issue issue = mongoOperations.findOne(new Query(Criteria.where("id").is(issueId)), Issue.class);
        if (issue != null) {
            mongoOperations.updateFirst(new Query(Criteria.where("id").is(issueId)), Update.update("completedTime",
                    completedTime), Issue.class);
            mongoOperations.updateFirst(new Query(Criteria.where("id").is(issueId)), Update.update("isDone", true),
                    Issue.class);
            return ResponseEntity.ok().body(issueId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
        }
    }

    // Returnerar medelvärdet för estimerad tid.
    public ResponseEntity<?> getEstimatedTime(String userId, String issueId, String sessionId) {
        if (authenticateUser(userId, sessionId) == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }
        Issue issue = mongoOperations.findOne(new Query(Criteria.where("id").is(issueId)), Issue.class);
        if (issue != null) {
            int estimatedTime = 0;
            for (int i : issue.getEstimatedTimes().values()) {
                estimatedTime += i;
            }
            return ResponseEntity.ok().body(estimatedTime / issue.getEstimatedTimes().size());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
        }
    }
}
