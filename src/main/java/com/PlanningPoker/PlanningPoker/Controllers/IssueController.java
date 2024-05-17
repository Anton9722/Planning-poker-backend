package com.PlanningPoker.PlanningPoker.Controllers;

import com.PlanningPoker.PlanningPoker.models.Issue;
import com.PlanningPoker.PlanningPoker.service.IssueService;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class IssueController {
    private IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping("project/{projectId}/issues")
    public List<Issue> getProjectIssues(@PathVariable UUID projectId) {
        return issueService.getProjectIssues(projectId);
    }

    @GetMapping("project/issue/{issueId}")
    public Issue getIssueById(@PathVariable UUID issueId) {
        return issueService.getIssueById(issueId);
    }

    @PostMapping("/issue")
    public Issue createIssue(@RequestBody Issue issue) {
        return issueService.createIssue(issue);
    }

    @DeleteMapping("/issue/{issueId}")
    public Issue deleteIssue(@PathVariable UUID issueId) {
        return issueService.deleteIssue(issueId);
    }

    @PostMapping("/issue/{issueId}")
    public Issue estimateTime(@PathVariable UUID issueId) {
        return issueService.estimateTime(issueId);
    }

    @PostMapping("/issue/{issueId}")
    public Issue closeIssue(@PathVariable UUID issueId) {
        return issueService.closeIssue(issueId);
    }
}
