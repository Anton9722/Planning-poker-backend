package com.PlanningPoker.PlanningPoker.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.PlanningPoker.PlanningPoker.models.Project;
import com.PlanningPoker.PlanningPoker.service.ProjectService;

@CrossOrigin(origins = "*")
@RestController
public class ProjectController {
	private ProjectService projectService;

	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	@GetMapping("/project/{ProjectId}")
	public ResponseEntity<?> getProjectById(
			@PathVariable String ProjectId, 
			@RequestHeader String sessionId, 
			@RequestHeader String userId) {
		return projectService.getProjectById(ProjectId,sessionId,userId);
	}

	// @GetMapping("/project/list")
	// public List<Project> getProjectList() {
	// 	//
	// 	return projectService.getProjectList();
	// }

	@PostMapping("/project/create")
	public ResponseEntity<?> createProject(
			@RequestBody Project project, 
			@RequestHeader String userId, 
			@RequestHeader String sessionId) {
		return projectService.createProject(project, userId, sessionId);
	}

	@DeleteMapping("/project/delete")
	public ResponseEntity<?> deleteProject(
			@RequestHeader String projectId,
			@RequestHeader String userId, 
			@RequestHeader String sessionId) {
		return projectService.deleteProject(projectId, userId, sessionId); 
	}

	@PutMapping("/project/addmember") 
	public ResponseEntity<?> addMemberToProject(
			@RequestHeader String userId, 
			@RequestHeader String usernameToAdd, 
			@RequestHeader String projectId,
			@RequestHeader String sessionId) {
		return projectService.addMemberToProject(userId, usernameToAdd, projectId, sessionId);
	}
	
	@DeleteMapping("/project/removemember")
	public ResponseEntity<?> removeMemberFromProject(
			@RequestHeader String userId,
			@RequestHeader String usernameToRemove,
			@RequestHeader String projectId,
			@RequestHeader String sessionId,
			@RequestHeader String userIdToRemove) {
		return projectService.removeMemberFromProject(userId, usernameToRemove, projectId, sessionId, userIdToRemove);
	}
}
