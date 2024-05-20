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
	

	private List<String> memberList;

	public Project(String id, String creatorId, String name, List<String> memberList) {
		this.id = id;
		this.name = name;
		this.creatorId = creatorId;
		this.memberList = memberList != null ? memberList : new ArrayList<>();
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

	public List<String> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<String> memberList) {
		this.memberList = memberList;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	
}
