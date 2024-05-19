package com.PlanningPoker.PlanningPoker.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.PlanningPoker.PlanningPoker.models.Issue;
import com.PlanningPoker.PlanningPoker.service.IssueService;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin("*")
@RestController
public class IssueController {
    private IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    // Returnerar alla issues från ett projekt.
    @GetMapping("project/{projectId}/issues")
    public ResponseEntity<?> getProjectIssues(
            @PathVariable("projectId") String projectId,
            @RequestHeader("sessionID") String sessionId) {
        return issueService.getProjectIssues(projectId, sessionId);
    }

    // Returnerar ett issue.
    @GetMapping("project/issue/{issueId}")
    public ResponseEntity<?> getIssueById(
            @PathVariable("issueId") String issueId,
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
    @DeleteMapping("project/issue/{issueId}")
    public ResponseEntity<?> deleteIssue(
            @PathVariable("issueId") String issueId,
            @RequestHeader("sessionID") String sessionId) {
        return issueService.deleteIssue(issueId, sessionId);
    }

    // Tilldelar en medlem till ett issue.
    @PatchMapping("project/issue/assign-member/{issueId}")
    public ResponseEntity<?> assignIssue(
            @PathVariable("issueId") String issueId,
            @RequestHeader("userId") String userId,
            @RequestHeader("sessionID") String sessionId) {
        return issueService.assignIssue(userId, issueId, sessionId);
    }

    // Tilldelar en tid till ett issue.
    @PatchMapping("project/issue/assign-time/{issueId}")
    public ResponseEntity<?> assignTime(
            @PathVariable("issueId") String issueId,
            @RequestHeader("userId") String userId,
            @RequestHeader("estimatedTime") int estimatedTime,
            @RequestHeader("sessionID") String sessionId) {
        return issueService.assignTime(userId, issueId, estimatedTime, sessionId);
    }

    // Stänger ett issue.
    @PatchMapping("project/issue/close/{issueId}")
    public ResponseEntity<?> closeIssue(
            @PathVariable("issueId") String issueId,
            @RequestHeader("sessionID") String sessionId) {
        return issueService.closeIssue(issueId, sessionId);
    }

    // Returnerar medelvärdet för estimerad tid.
    @GetMapping("project/issue/estimatedTime/{issueId}")
    public ResponseEntity<?> getEstimatedTime(
            @PathVariable("issueId") String issueId,
            @RequestHeader("sessionID") String sessionId) {
        return issueService.getEstimatedTime(issueId, sessionId);
    }
}