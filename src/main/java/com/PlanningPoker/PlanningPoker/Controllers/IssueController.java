package com.PlanningPoker.PlanningPoker.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.PlanningPoker.PlanningPoker.models.Issue;
import com.PlanningPoker.PlanningPoker.service.IssueService;

@CrossOrigin("*")
@RestController
public class IssueController {
    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    // Returnerar alla issues från ett projekt.
    @GetMapping("project/{projectId}/issues")
    public ResponseEntity<?> getIssues(
            @RequestHeader("userId") String userId,
            @PathVariable("projectId") String projectId,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.getProjectIssues(userId, projectId, sessionId);
    }

    // Tar bort alla issues från ett projekt.
    @DeleteMapping("project/{projectId}/issues")
    public ResponseEntity<?> deleteIssues(
            @RequestHeader("userId") String userId,
            @PathVariable("projectId") String projectId,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.deleteProjectIssues(userId, projectId, sessionId);
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
    public ResponseEntity<?> assignMember(
            @RequestHeader("userId") String userId,
            @PathVariable("issueId") String issueId,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.assignMember(userId, issueId, sessionId);
    }

    // Tilldelar en estimerad tid till ett issue.
    @PatchMapping("project/issue/assign-estimated-time/{issueId}")
    public ResponseEntity<?> assignEstimatedTime(
            @RequestHeader("userId") String userId,
            @PathVariable("issueId") String issueId,
            @RequestHeader("estimatedTime") int estimatedTime,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.assignEstimatedTime(userId, issueId, estimatedTime, sessionId);
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

    // Tar bort en tilldelad medlem från alla issues i ett projekt.
    @DeleteMapping("project/{projectId}/issue/remove-assigned-member")
    public ResponseEntity<?> removeAssignedMemberFromProject(
            @RequestHeader("userId") String userId,
            @RequestHeader("userIdToRemove") String userIdToRemove,
            @PathVariable("projectId") String projectId,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.removeAssignedMemberFromProject(userId, projectId, sessionId, userIdToRemove);
    }

    // Tar bort en estimerad tid från alla issues i ett projekt.
    @DeleteMapping("project/{projectId}/issue/remove-estimated-time")
    public ResponseEntity<?> removeEstimatedTimeFromProject(
            @RequestHeader("userId") String userId,
            @RequestHeader("userIdToRemove") String userIdToRemove,
            @PathVariable("projectId") String projectId,
            @RequestHeader("sessionId") String sessionId) {
        return issueService.removeEstimatedTimeFromProject(userId, projectId, sessionId, userIdToRemove);
    }
}