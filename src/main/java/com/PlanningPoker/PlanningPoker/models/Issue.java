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
    private String assignedId;
    private Integer completedTime;
    private Map<String, Object> estimatedTimes;

    public Issue(String id, String name, String projectId, String creatorId, Map<String, Object> estimatedTimes) {
        this.id = id;
        this.projectId = projectId;
        this.creatorId = creatorId;
        this.name = name;
        this.completedTime = null;
        this.assignedId = null;
        this.estimatedTimes = estimatedTimes != null ? estimatedTimes : new HashMap<String, Object>();
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

    public Integer getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Integer completedTime) {
        this.completedTime = completedTime;
    }

    public String getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(String assignedId) {
        this.assignedId = assignedId;
    }

    public Map<String, Object> getEstimatedTimes() {
        return estimatedTimes;
    }

    public void setEstimatedTimes(Map<String, Object> estimatedTimes) {
        this.estimatedTimes = estimatedTimes;
    }

    public void setModifiedEstimatedTimes(String userId) {
        this.estimatedTimes.forEach((k, v) -> {
            if (v == null) {
                this.estimatedTimes.put(k, false);
            } else if (k.equals(userId)) {
                this.estimatedTimes.put(k, v);
            } else {
                this.estimatedTimes.put(k, true);
            }
        });
    }
}