package com.PlanningPoker.PlanningPoker.Controllers;

import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.PlanningPoker.PlanningPoker.models.Issue;
import com.PlanningPoker.PlanningPoker.service.IssueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
public class IssueController {
    private IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    // Returnerar alla issues från ett projekt.
    @GetMapping("project/issues")
    public ResponseEntity<?> getProjectIssues(
            @RequestHeader("projectId") String projectId,
            @RequestHeader("sessionID") String sessionId) {
        return issueService.getProjectIssues(projectId, sessionId);
    }

    // Returnerar ett issue.
    @GetMapping("project/issue")
    public ResponseEntity<?> getIssueById(
            @RequestHeader("issueId") String issueId,
            @RequestHeader("sessionID") String sessionId) {
        return issueService.getIssueById(issueId, sessionId);
    }

    // Skapar ett issue.
    @PostMapping("project/issue")
    public ResponseEntity<?> createIssue(
            @RequestBody Issue issue,
            @RequestHeader("sessionID") String sessionId) {
        return issueService.createIssue(issue, sessionId);
    }

    // Tar bort ett issue.
    @DeleteMapping("project/issue")
    public ResponseEntity<?> deleteIssue(
            @RequestHeader("issueId") String issueId,
            @RequestHeader("sessionID") String sessionId) {
        return issueService.deleteIssue(issueId, sessionId);
    }

    // Tilldelar en medlem till ett issue.
    @PatchMapping("project/issue")
    public ResponseEntity<?> assignIssue(
            @RequestHeader("userId") String userId,
            @RequestHeader("issueId") String issueId,
            @RequestHeader("sessionID") String sessionId) {
        return issueService.assignIssue(userId, issueId, sessionId);
    }

    // Sätter estimerad tid.
    @PatchMapping("project/issue")
    public ResponseEntity<?> estimateTime(
            @RequestHeader("userId") String userId,
            @RequestHeader("issueId") String issueId,
            @RequestHeader("estimatedTime") int estimatedTime,
            @RequestHeader("sessionID") String sessionId) {
        return issueService.estimateTime(userId, issueId, estimatedTime, sessionId);
    }

    // Avslutar ett issue.
    @PatchMapping("project/issue")
    public ResponseEntity<?> closeIssue(
            @RequestHeader("issueId") String issueId,
            @RequestHeader("sessionID") String sessionId) {
        return issueService.closeIssue(issueId, sessionId);
    }
}