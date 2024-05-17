package com.PlanningPoker.PlanningPoker.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.PlanningPoker.PlanningPoker.models.LoginRequest;
import com.PlanningPoker.PlanningPoker.models.User;
import com.PlanningPoker.PlanningPoker.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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

    @GetMapping("/user/get-user-by-id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> checkLogin(@RequestBody LoginRequest loginRequest) {
        return userService.checkLogin(loginRequest);
    }
    
}
