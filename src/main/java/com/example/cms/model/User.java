package com.example.cms.model;

import java.util.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String userName;
	private String email; 
	private String password;
	private boolean deleted;
	@CreatedDate
	@Column(updatable = false)
	private String createdAt;
	@LastModifiedDate
	private String lastModifiedAt;
	
	@OneToMany(mappedBy = "user")
	private List<Blog> list=new ArrayList<>();
	
	
	public List<Blog> getList() {
		return list;
	}
	public void setList(List<Blog> list) {
		this.list = list;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public User setDeleted(boolean deleted) {
		this.deleted = deleted;
		return this;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getLastModifiedAt() {
		return lastModifiedAt;
	}
	public void setLastModifiedAt(String lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

}
