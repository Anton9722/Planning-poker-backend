package com.PlanningPoker.PlanningPoker.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.PlanningPoker.PlanningPoker.models.LoginRequest;
import com.PlanningPoker.PlanningPoker.models.Project;
import com.PlanningPoker.PlanningPoker.models.User;
import com.PlanningPoker.PlanningPoker.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;



@CrossOrigin(origins = "*")
@RestController
public class UserController {
    
    private UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping //TESTMAPPING FÖR UTVECKLING
    public List<User> testMapping () {
        return userService.test();
    }

    @PostMapping("/user/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/user/get-user-by-id")
    public ResponseEntity<?> getUserById(@RequestHeader("id") String id, @RequestHeader("sessionID") String sessionId) {
        return userService.getUserById(id, sessionId);
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> checkLogin(@RequestBody LoginRequest loginRequest) {
        return userService.checkLogin(loginRequest);
    }
    
    @GetMapping("user/get-projectList")
    public ResponseEntity<?> getProjectList(@RequestHeader("id") String id, @RequestHeader("sessionID") String sessionId) {
        return userService.getProjectList(id, sessionId);
    }

    @PatchMapping("user/add-project")
    public ResponseEntity<?> addProjectToProjectList(@RequestHeader("id") String id, @RequestHeader("sessionID") String sessionId, @RequestHeader String projectId) {
        return userService.addProjectToProjectList(id, sessionId, projectId);
    }

    @PostMapping("user/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("id") String id, @RequestHeader("sessionID") String sessionId) {
        return userService.logoutUser(id, sessionId);
    }
}
