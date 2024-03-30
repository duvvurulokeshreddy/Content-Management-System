package com.example.cms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.*;

@Entity
public class ContributionPanel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int panelld;

	@ManyToMany
	public List<User> list=new ArrayList<>();
	
	public List<User> getList() {
		return list;
	}

	public void setList(List<User> list) {
		this.list = list;
	}

	public int getPanelld() {
		return panelld;
	}

	public void setPanelld(int panelld) {
		this.panelld = panelld;
	}
}
