package com.PlanningPoker.PlanningPoker.service;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.PlanningPoker.PlanningPoker.models.Project;

@Service
public class ProjectService {
	private final MongoOperations mongoOperations;

	public ProjectService(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}
	
	public Project getProjectById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		
		return mongoOperations.findOne(query, Project.class);
	}

	public Project getProjectByUserId(String userId) {
		// User user = getUserById(userId);
		return null;

	}

	public List<Project> getProjectList() {
		return mongoOperations.findAll(Project.class);
	}
	
	public Project createProject(Project project, String userId) {
		project.setCreatorId(userId);
		return mongoOperations.insert(project);
	}

	public String deleteProject(String id) {
		Query query = Query.query(Criteria.where("id").is(id));
		mongoOperations.remove(query, Project.class);
		return id + " removed";
		
	}

	// public String addMemberToProject(String userId, String projectId) {
	// 	Project project = getProjectById(projectId);
	// 	User user = getUserById(userId);
	// 	project.getMemberList().add(user);
	// 	mongoOperations.save(project);
	// 	return "User: " + user.getUsername() + " added to " + project.getName();
	// }
	
	// public String removeMemberFromProject(String userId, String projectId) {
	// 	Project project = getProjectById(projectId);
	// 	User user = getUserById(userId);
	// 	project.getMemberList().remove(user);
	// 	mongoOperations.save(project);
	// 	return "User: " + user.getUsername() + " removed from " + project.getName();
	// }
	
	
}
