package com.PlanningPoker.PlanningPoker.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Projects")
public class Project {
	@Id
	private String id;
	private String creatorId;
	private String name;
	

	private List<User> memberList = new ArrayList<>();

	public Project(String id, String creatorId, String name, List<User> memberList) {
		this.id = id;
		this.name = name;
		this.creatorId = creatorId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<User> memberList) {
		this.memberList = memberList;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	
}
