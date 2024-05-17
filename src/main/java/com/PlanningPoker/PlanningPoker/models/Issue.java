package com.PlanningPoker.PlanningPoker.models;

import java.util.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Issues")
public class Issue {
    @Id
    private String id;
    private String projectId;
    private String creatorId;
    private String name;
    private boolean isDone;
    private int completedTime;
    private List<String> assignedIds;
    private Map<String, Integer> estimatedTimes;

    public Issue(String id, String name, String projectId, String creatorId) {
        this.id = id;
        this.projectId = projectId;
        this.creatorId = creatorId;
        this.name = name;
        this.isDone = false;
        this.completedTime = 0;
        this.assignedIds = new ArrayList<>();
        this.estimatedTimes = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
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

    public int getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(int completedTime) {
        this.completedTime = completedTime;
    }

    public List<String> getAssignedIds() {
        return assignedIds;
    }

    public void setAssignedIds(List<String> assignedIds) {
        this.assignedIds = assignedIds;
    }

    public Map<String, Integer> getEstimatedTimes() {
        return estimatedTimes;
    }

    public void setEstimatedTimes(Map<String, Integer> estimatedTimes) {
        this.estimatedTimes = estimatedTimes;
    }
}