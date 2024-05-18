package com.PlanningPoker.PlanningPoker.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.PlanningPoker.PlanningPoker.models.LoginRequest;
import com.PlanningPoker.PlanningPoker.models.User;

@Service
public class UserService {
    private final MongoOperations mongoOperations;

    public UserService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public List<User> test () {  //TESTMETOD FÖR UTVECKLING
        return mongoOperations.findAll(User.class);
    }

    //Skapa ny user
    public ResponseEntity<?> createUser(User user) {
        
        String username = user.getUsername();
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));

        //checkar om det redan finns en user med det användarnamnet
        if(mongoOperations.findOne(query, User.class) == null) {

            User createdUser = mongoOperations.insert(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

        } else {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("User alredy exists.");

        }
    }

    //hämta user från id
    public ResponseEntity<?> getUserById(String id, String sessionId) {

        if (authenticateUser(id, sessionId) == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        User user = mongoOperations.findOne(query, User.class);

        if(user == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no user with ID \"" + id + "\" was found");

        } else {
            
            return ResponseEntity.ok(user);

        }
    }

    //check login
    public ResponseEntity<?> checkLogin(LoginRequest loginRequest) {

        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(loginRequest.getUsername()));
        User userToCheck = mongoOperations.findOne(query, User.class);

        if(userToCheck == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username");

        }

        if(userToCheck.isPasswordCorrect(loginRequest.getPassword())) {

            userToCheck.setSessionId(UUID.randomUUID().toString());
            mongoOperations.save(userToCheck);

            Map<String, String> loginResponse = new HashMap<>();
            loginResponse.put("id", userToCheck.getId());
            loginResponse.put("sessionId", userToCheck.getSessionId());

            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);

        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");

        }
    }
    //authenticate
    public boolean authenticateUser(String id, String sessionId) {

        if(id == null || sessionId == null || id.isEmpty() || sessionId.isEmpty()) {
            return false;
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        User foundUser = mongoOperations.findOne(query, User.class);

        if(foundUser != null && foundUser.getSessionId() != null && foundUser.getSessionId().equals(sessionId)) {
            return true;
        } else {
            return false;
        }

    }

    //hämtar projects listan
    public ResponseEntity<?> getProjectList(String id, String sessionId) {

        if (authenticateUser(id, sessionId) == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }
        
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        User user = mongoOperations.findOne(query, User.class);
        
        return ResponseEntity.ok(user.getProjectList());
    }

    //lägg till project i project listan
    public ResponseEntity<?> addProjectToProjectList(String id, String sessionId, Map<String, String> project) {

        if (authenticateUser(id, sessionId) == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        User user = mongoOperations.findOne(query, User.class);

        user.addProject(project);
        mongoOperations.save(user);

        return ResponseEntity.ok().body("Project added to project list");

    }

    //logga ut
    public ResponseEntity<?> logoutUser(String id, String sessionId) {

        if (authenticateUser(id, sessionId) == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        User user = mongoOperations.findOne(query, User.class);

        user.setSessionId(null);
        mongoOperations.save(user);

        return ResponseEntity.ok().body("Logout successful");

    }

}
