package com.PlanningPoker.PlanningPoker.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@GetMapping("/project/id")
	public Project getProjectById(@PathVariable String id) {
		return projectService.getProjectById(id);
	}

	@GetMapping("/project/list")
	public List<Project> getProjectList() {
		return projectService.getProjectList();
	}

	@PostMapping("/project/create/{userId}")
	public Project createProject(@RequestBody Project project, @PathVariable String userId) {
		return projectService.createProject(project, userId);
	}
	@DeleteMapping("/project/delete/{activityId}")
	public String deleteProject(@PathVariable String activityId) {
		return projectService.deleteProject(activityId); 
	}

	//behöver user
	// @PutMapping("/project/addmember/{userId}/{projectId}") 
	// public String addMemberToProject(@PathVariable UUID userId, @PathVariable UUID projectId) {
	// 	return projectService.addMemberToProject(userId, projectId);
	// }
	// @DeleteMapping("/project/removemember/{userId}/{projectId}") 
	// public String removeMemberFromProject(@PathVariable UUID userId, @PathVariable UUID projectId) {
	// 	return projectService.removeMemberFromProject(userId, projectId);
	// }


}
