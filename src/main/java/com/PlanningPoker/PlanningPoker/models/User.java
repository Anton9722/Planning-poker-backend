package com.PlanningPoker.PlanningPoker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Document(collection="Users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private List<Map<String,String>> projectList;
    private String sessionId;
    
    public User(String id, String username, List<Map<String,String>> projectList, String sessionId) {
        this.id = id;
        this.username = username;
        this.sessionId = sessionId;
        this.projectList = projectList != null ? projectList : new ArrayList<>();
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public boolean isPasswordCorrect(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, getPassword());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public List<Map<String,String>> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Map<String,String>> projectList) {
        this.projectList = projectList;
    }

    public void addProject(Map<String,String> project) {
        projectList.add(project);
    }

    public void removeProject(Map<String,String> project) {
        projectList.remove(project);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
}
