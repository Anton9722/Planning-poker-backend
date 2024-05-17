package com.PlanningPoker.PlanningPoker.models;

import java.util.*;
import org.springframework.data.annotation.Id;

public class Issue {
    @Id
    private UUID issueId;
    private UUID projectId;
    private UUID creatorId;
    private String name;
    private boolean isDone;
    private long completedTime;
    private List<UUID> assignedIds;
    private Map<UUID, Long> estimatedTimes;

    public Issue(String name, UUID projectId, UUID creatorId) {
        this.issueId = UUID.randomUUID();
        this.projectId = projectId;
        this.creatorId = creatorId;
        this.name = name;
        this.isDone = false;
        this.completedTime = 0;
        this.assignedIds = new ArrayList<>();
        this.estimatedTimes = new HashMap<>();
    }

    public UUID getIssueId() {
        return issueId;
    }

    public void setIssueId(UUID issueId) {
        this.issueId = issueId;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public long getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(long completedTime) {
        this.completedTime = completedTime;
    }

    public List<UUID> getAssignedIds() {
        return assignedIds;
    }

    public void setAssignedIds(List<UUID> assignedIds) {
        this.assignedIds = assignedIds;
    }

    public Map<UUID, Long> getEstimatedTimes() {
        return estimatedTimes;
    }

    public void setEstimatedTimes(Map<UUID, Long> estimatedTimes) {
        this.estimatedTimes = estimatedTimes;
    }
}