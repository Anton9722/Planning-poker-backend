package com.PlanningPoker.service;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.PlanningPoker.models.User;

@Service
public class UserService {
    private final MongoOperations mongoOperations;

    public UserService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public List<User> test () {  //TESTMETOD FÖR UTVECKLING
        return mongoOperations.findAll(User.class);
    }
}
