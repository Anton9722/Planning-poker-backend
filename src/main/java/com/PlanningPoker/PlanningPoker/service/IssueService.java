package com.PlanningPoker.PlanningPoker.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.PlanningPoker.PlanningPoker.models.*;

@Service
public class IssueService {
    @Autowired
    private UserService userService;
    private final MongoOperations mongoOperations;

    public IssueService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    // Returnerar alla issues från ett projekt.
    public ResponseEntity<?> getProjectIssues(String userId, String projectId, String sessionId) {
        if (userService.getUserById(userId, sessionId).getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }

        List<Issue> issues = mongoOperations.findAll(Issue.class).stream()
                .filter((Issue issue) -> issue.getProjectId().equals(projectId)).toList();
        issues.forEach((Issue issue) -> {
            if (issue.getEstimatedTimes().containsValue(null)) {
                issue.setModifiedEstimatedTimes(userId);
            }
        });

        return !issues.isEmpty() ? ResponseEntity.ok().body(issues)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("No issues found.");
    }

    // Tar bort alla issues från ett projekt.
    public ResponseEntity<?> deleteProjectIssues(String userId, String projectId, String sessionId) {
        if (userService.getUserById(userId, sessionId).getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }

        List<Issue> issues = mongoOperations.findAll(Issue.class).stream()
                .filter((Issue issue) -> issue.getProjectId().equals(projectId)).toList();
        issues.forEach((Issue issue) -> mongoOperations.remove(new Query(Criteria.where("projectId").is(projectId)),
                Issue.class));

        return !issues.isEmpty() ? ResponseEntity.ok().body(issues)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("No issues found.");
    }

    // Skapar ett issue.
    public ResponseEntity<?> createIssue(String userId, Issue issue, String sessionId) {
        if (userService.getUserById(userId, sessionId).getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }

        return mongoOperations.insert(issue) != null ? ResponseEntity.ok().body(issue)
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Issue not created.");
    }

    // Tar bort ett issue.
    public ResponseEntity<?> deleteIssue(String userId, String issueId, String sessionId) {
        if (userService.getUserById(userId, sessionId).getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }

        Issue issue = mongoOperations.findOne(new Query(Criteria.where("id").is(issueId)), Issue.class);
        if (issue != null) {
            mongoOperations.remove(new Query(Criteria.where("id").is(issueId)), Issue.class);
            return ResponseEntity.ok().body(issue);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
        }
    }

    // Tilldelar en medlem till ett issue.
    public ResponseEntity<?> assignMember(String userId, String issueId, String sessionId) {
        if (userService.getUserById(userId, sessionId).getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }

        Issue issue = mongoOperations.findOne(new Query(Criteria.where("id").is(issueId)), Issue.class);
        if (issue != null && issue.getAssignedId() == null) {
            issue.setAssignedId(userId);
            mongoOperations.updateFirst(new Query(Criteria.where("id").is(issueId)),
                    Update.update("assignedId", userId), Issue.class);
            return ResponseEntity.ok().body(issue);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
        }
    }

    // Tilldelar en estimerad tid till ett issue.
    public ResponseEntity<?> assignEstimatedTime(String userId, String issueId, int estimatedTime, String sessionId) {
        if (userService.getUserById(userId, sessionId).getStatusCode() != HttpStatus.OK) {
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

    // Stänger ett issue och sätter slutförd tid.
    public ResponseEntity<?> closeIssue(String userId, String issueId, int completedTime, String sessionId) {
        if (userService.getUserById(userId, sessionId).getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }

        Issue issue = mongoOperations.findOne(new Query(Criteria.where("id").is(issueId)), Issue.class);
        if (issue != null && issue.getCompletedTime() == null) {
            issue.setCompletedTime(completedTime);
            mongoOperations.updateFirst(new Query(Criteria.where("id").is(issueId)),
                    Update.update("completedTime", completedTime), Issue.class);
            return ResponseEntity.ok().body(issue);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found.");
        }
    }

    // Tar bort en tilldelad medlem från alla issues i ett projekt.
    public ResponseEntity<?> removeAssignedMemberFromProject(String userId, String projectId, String sessionId,
            String userIdToRemove) {
        if (userService.getUserById(userId, sessionId).getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }

        List<Issue> issues = mongoOperations.findAll(Issue.class).stream()
                .filter((Issue issue) -> issue.getProjectId().equals(projectId) && issue.getAssignedId() != null
                        && issue.getAssignedId().equals(userIdToRemove))
                .toList();
        issues.forEach((Issue issue) -> {
            issue.setAssignedId(null);
            mongoOperations.updateFirst(new Query(Criteria.where("id").is(issue.getId())),
                    Update.update("assignedId", null), Issue.class);
        });

        return !issues.isEmpty() ? ResponseEntity.ok().body(issues)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("No issues found.");
    }

    // Tar bort en estimerad tid från alla issues i ett projekt.
    public ResponseEntity<?> removeEstimatedTimeFromProject(String userId, String projectId, String sessionId,
            String userIdToRemove) {
        if (userService.getUserById(userId, sessionId).getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }

        List<Issue> issues = mongoOperations.findAll(Issue.class).stream()
                .filter((Issue issue) -> issue.getProjectId().equals(projectId)
                        && issue.getEstimatedTimes().containsKey(userIdToRemove))
                .toList();
        issues.forEach((Issue issue) -> {
            issue.getEstimatedTimes().remove(userIdToRemove);
            mongoOperations.updateFirst(new Query(Criteria.where("id").is(issue.getId())),
                    Update.update("estimatedTimes", issue.getEstimatedTimes()), Issue.class);
        });

        return !issues.isEmpty() ? ResponseEntity.ok().body(issues)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("No issues found.");
    }
}
