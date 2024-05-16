package com.PlanningPoker.PlanningPoker.service;

import java.util.List;

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
    public ResponseEntity<?> getUserById(String id) {

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

            return ResponseEntity.status(HttpStatus.OK).body(userToCheck.getId());

        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");

        }
    }

}
