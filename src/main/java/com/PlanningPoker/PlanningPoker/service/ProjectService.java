package com.PlanningPoker.PlanningPoker.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.PlanningPoker.PlanningPoker.models.Project;
import com.PlanningPoker.PlanningPoker.models.User;

@Service
public class ProjectService {
	private final MongoOperations mongoOperations;

	@Autowired
	private UserService userService;

	public ProjectService(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}
	
	public ResponseEntity<?> getProjectById(String projectId, String sessionId, String userId) {

		ResponseEntity<?> userResponse = userService.getUserById(userId, sessionId);
			if (userResponse.getStatusCode() != HttpStatus.OK) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
			}

		Project project = mongoOperations.findOne(new Query(Criteria.where("id").is(projectId)), Project.class);

		if ( project != null ) {
			return ResponseEntity.ok().body(project);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
		}
	}


	// public List<Project> getProjectList() {
	// 	return mongoOperations.findAll(Project.class);
	// }
	
	public ResponseEntity<?> createProject(Project project, String userId, String sessionId) {	
		ResponseEntity<?> userResponse = userService.getUserById(userId, sessionId);
			if (userResponse.getStatusCode() != HttpStatus.OK) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
			}

			User user = (User) userResponse.getBody();
			if (user == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get User information");
			}

			project.setCreatorId(userId);

			Map<String, String> userToAddToList = new HashMap<>();
			userToAddToList.put("userID", user.getId());
			userToAddToList.put("userName", user.getUsername());
			project.addMember(userToAddToList);

			Project newProject = mongoOperations.insert(project);

			if (newProject != null) {
				Map<String, String> newProjectToAdd = new HashMap<>();
				newProjectToAdd.put("projectName", newProject.getName());
				newProjectToAdd.put("projectID", newProject.getId());
				userService.addProjectToProjectList(userId, sessionId, newProjectToAdd);

				return ResponseEntity.ok().body(newProject);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Project not created");
			}
	}

	public ResponseEntity<?> deleteProject(String projectId, String userId, String sessionId) {

		ResponseEntity<?> userResponse = userService.getUserById(userId, sessionId);
			if (userResponse.getStatusCode() != HttpStatus.OK) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
			}
			
			User user = (User) userResponse.getBody();
			if (user == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get User information");
			}

			Project projectToDelete = mongoOperations.findById(projectId, Project.class);
			if (projectToDelete == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("project not found");
			}

			//bara creator som kan delete
			if (!projectToDelete.getCreatorId().equals(userId)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not the creator");
			}
				userService.removeProjectFromProjectList(userId, sessionId, projectToDelete.getId());
				mongoOperations.remove(projectToDelete);
				mongoOperations.save(user);	
				return ResponseEntity.ok().body("Project " + projectId + " removed!");

			//TODO - någon check för att delete i allas projectlist som har det projectet
	
	}

	public ResponseEntity<?> addMemberToProject(String userId, String userIdToAdd, String projectId, String sessionId) {

		ResponseEntity<?> userResponse = userService.getUserById(userId, sessionId);
			if (userResponse.getStatusCode() != HttpStatus.OK) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
			}

		ResponseEntity<?> projectResponse = getProjectById(projectId,sessionId,userId);
			if (projectResponse.getStatusCode() != HttpStatus.OK) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not FOund!");
			}

		Project project = (Project) projectResponse.getBody();
		//hitta user
		if (project.getMemberList().contains(userIdToAdd)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is allready in that project");
		}

		User userToAdd = mongoOperations.findOne(new Query(Criteria.where("id").is(userIdToAdd)), User.class);
		if (userToAdd == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		Map<String, String> newProjectToAdd = new HashMap<>();
		newProjectToAdd.put("projectName", project.getName());
		newProjectToAdd.put("projectID", project.getId());
		userToAdd.addProject(newProjectToAdd);

		Map<String, String> userToAddToList = new HashMap<>();
		userToAddToList.put("userID", userToAdd.getId());
		userToAddToList.put("userName", userToAdd.getUsername());
		project.addMember(userToAddToList);

		mongoOperations.save(project);
		mongoOperations.save(userToAdd);

		return ResponseEntity.ok().body("User: " + userToAdd.getUsername() + " added to " + project.getName());
	}
	

	public ResponseEntity<?> removeMemberFromProject(String userId, String userIdToRemove, String projectId, String sessionId) {
		
		ResponseEntity<?> userResponse = userService.getUserById(userId, sessionId);

			if (userResponse.getStatusCode() != HttpStatus.OK) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
			}

		ResponseEntity<?> projectResponse = getProjectById(projectId,sessionId,userId);

			if (projectResponse.getStatusCode() != HttpStatus.OK) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not FOund!");
			}

			Project project = (Project) projectResponse.getBody();

			User userToRemove = mongoOperations.findOne(new Query(Criteria.where("id").is(userIdToRemove)), User.class);
			if (userToRemove == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
			}
			Project projectToRemove = mongoOperations.findOne(new Query(Criteria.where("id").is(projectId)),Project.class);
			if (projectToRemove == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
			}

		for (Map<String, String> projectInList : userToRemove.getProjectList()) {
            if(projectInList.get("projectID").equals(projectToRemove.getId())) {
                userToRemove.removeProject(projectInList);
            }
        }
		
		project.getMemberList().remove(userToRemove.getId());
		
		mongoOperations.save(userToRemove);
		mongoOperations.save(project);
		

		return ResponseEntity.ok().body("User: " + userToRemove.getUsername() + " removed from " + project.getName());
	}
	
}
