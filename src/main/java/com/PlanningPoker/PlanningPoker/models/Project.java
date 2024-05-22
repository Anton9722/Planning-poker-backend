package com.PlanningPoker.PlanningPoker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Projects")
public class Project {
	@Id
	private String id;
	private String creatorId;
	private String name;
	
	private List<Map<String,String>> memberList;

	public Project(String id, String creatorId, String name, List<Map<String,String>> memberList) {
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

	public List<Map<String,String>> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<Map<String,String>> memberList) {
		this.memberList = memberList;
	}

	public void addMember(Map<String, String> member) {
		memberList.add(member);
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	
}
