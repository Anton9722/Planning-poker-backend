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
            @RequestHeader("userId") String userId,
            @PathVariable("projectId") String projectId,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.getProjectIssues(userId, projectId, sessionId);
    }

    // Returnerar ett issue.
    @GetMapping("project/issue/{issueId}")
    public ResponseEntity<?> getIssueById(
            @RequestHeader("userId") String userId,
            @PathVariable("issueId") String issueId,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.getIssueById(userId, issueId, sessionId);
    }

    // Skapar ett issue.
    @PostMapping("project/issue")
    public ResponseEntity<?> createIssue(
            @RequestHeader("userId") String userId,
            @RequestBody Issue issue,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.createIssue(userId, issue, sessionId);
    }

    // Tar bort ett issue.
    @DeleteMapping("project/issue/{issueId}")
    public ResponseEntity<?> deleteIssue(
            @RequestHeader("userId") String userId,
            @PathVariable("issueId") String issueId,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.deleteIssue(userId, issueId, sessionId);
    }

    // Tilldelar en medlem till ett issue.
    @PatchMapping("project/issue/assign-member/{issueId}")
    public ResponseEntity<?> assignIssue(
            @RequestHeader("userId") String userId,
            @PathVariable("issueId") String issueId,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.assignIssue(userId, issueId, sessionId);
    }

    // Tilldelar en tid till ett issue.
    @PatchMapping("project/issue/assign-time/{issueId}")
    public ResponseEntity<?> assignTime(
            @RequestHeader("userId") String userId,
            @PathVariable("issueId") String issueId,
            @RequestHeader("estimatedTime") int estimatedTime,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.assignTime(userId, issueId, estimatedTime, sessionId);
    }

    // Stänger ett issue och sätter slutförd tid.
    @PatchMapping("project/issue/close/{issueId}")
    public ResponseEntity<?> closeIssue(
            @RequestHeader("userId") String userId,
            @PathVariable("issueId") String issueId,
            @RequestHeader("completedTime") int completedTime,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.closeIssue(userId, issueId, completedTime, sessionId);
    }

    // Returnerar medelvärdet för estimerad tid.
    @GetMapping("project/issue/estimatedTime/{issueId}")
    public ResponseEntity<?> getEstimatedTime(
            @RequestHeader("userId") String userId,
            @PathVariable("issueId") String issueId,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.getEstimatedTime(userId, issueId, sessionId);
    }
}