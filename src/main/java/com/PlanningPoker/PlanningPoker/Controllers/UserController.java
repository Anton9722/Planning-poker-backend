package com.PlanningPoker.PlanningPoker.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PlanningPoker.PlanningPoker.models.User;
import com.PlanningPoker.PlanningPoker.service.UserService;


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
}
