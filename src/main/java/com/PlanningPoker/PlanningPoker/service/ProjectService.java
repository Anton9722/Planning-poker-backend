package com.PlanningPoker.PlanningPoker.service;

import java.util.HashMap;
import java.util.List;
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
			userToAddToList.put("userId", user.getId());
			userToAddToList.put("username", user.getUsername());
			project.addMember(userToAddToList);

			Project newProject = mongoOperations.insert(project);

			if (newProject != null) {
				Map<String, String> newProjectToAdd = new HashMap<>();
				newProjectToAdd.put("projectname", newProject.getName());
				newProjectToAdd.put("projectId", newProject.getId());
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
			//Ta bort projekts från användarnas listor
			Query query = new Query(Criteria.where("projectList.projectId").in(projectId));
			List <User> usersWithProject = mongoOperations.find(query, User.class);
				for (User u : usersWithProject) {
					u.getProjectList().removeIf(project -> projectId.equals(project.get("projectId")));
					mongoOperations.save(u);
				}

				// user.getProjectList().remove(projectId);
				mongoOperations.remove(projectToDelete);
				return ResponseEntity.ok().body("Project " + projectId + " removed!");
	}

	public ResponseEntity<?> addMemberToProject(String userId, String usernameToAdd, String projectId, String sessionId) {

		ResponseEntity<?> userResponse = userService.getUserById(userId, sessionId);
			if (userResponse.getStatusCode() != HttpStatus.OK) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
			}

		ResponseEntity<?> projectResponse = getProjectById(projectId,sessionId,userId);
			if (projectResponse.getStatusCode() != HttpStatus.OK) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not FOund!");
			}

		Project project = (Project) projectResponse.getBody();
		if (project == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found!");
		}
	
		 // kolla om user redan är tillagd
		 boolean memberExists = project.getMemberList().stream()
		 	.anyMatch(member -> usernameToAdd.equals(member.get("username")));
	 	if (memberExists) {
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already in the project");
	 	}

		User userToAdd = mongoOperations.findOne(new Query(Criteria.where("username").is(usernameToAdd)), User.class);
		if (userToAdd == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		Map<String, String> newProjectToAdd = new HashMap<>();
		newProjectToAdd.put("projectname", project.getName());
		newProjectToAdd.put("projectId", project.getId());
		userToAdd.addProject(newProjectToAdd);

		Map<String, String> userToAddToList = new HashMap<>();
		userToAddToList.put("userId", userToAdd.getId());
		userToAddToList.put("username", userToAdd.getUsername());
		project.addMember(userToAddToList);

		mongoOperations.save(project);
		mongoOperations.save(userToAdd);

		return ResponseEntity.ok().body("User: " + userToAdd.getUsername() + " added to " + project.getName());
	}
	

	public ResponseEntity<?> removeMemberFromProject(String userId, String usernameToRemove, String projectId, String sessionId) {
		
		ResponseEntity<?> userResponse = userService.getUserById(userId, sessionId);

			if (userResponse.getStatusCode() != HttpStatus.OK) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: User authentication failed");
			}

		ResponseEntity<?> projectResponse = getProjectById(projectId,sessionId,userId);

			if (projectResponse.getStatusCode() != HttpStatus.OK) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not FOund!");
			}

			Project project = (Project) projectResponse.getBody();

			User userToRemove = mongoOperations.findOne(new Query(Criteria.where("username").is(usernameToRemove)), User.class);
			if (userToRemove == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
			}
			Project projectToRemove = mongoOperations.findOne(new Query(Criteria.where("id").is(projectId)),Project.class);
			if (projectToRemove == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
			}

		// kolla om user finns i listan
		boolean memberExists = project.getMemberList().stream()
        	.anyMatch(member -> usernameToRemove.equals(member.get("username")));
		if (!memberExists) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not a member of the project");
		}
		
		 // ta bort från projectList
		 userToRemove.getProjectList().removeIf(projectMap -> projectId.equals(projectMap.get("projectId")));

		 // ta bort från memberList
		 project.getMemberList().removeIf(member -> usernameToRemove.equals(member.get("username")));
		
		mongoOperations.save(userToRemove);
		mongoOperations.save(project);
		

		return ResponseEntity.ok().body("User: " + userToRemove.getUsername() + " removed from " + project.getName());
	}	
}
