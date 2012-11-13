package com.valdemar.tellatale.model;

import java.util.List;

import com.stackmob.sdk.model.StackMobUser;

public class User extends StackMobUser {
	private String email;
	private List<Tale> tales;
	public Dummy[] dummies; 


//	public TaleUser(String username, String password, String email) {
//
//		this(username, password);
//		this.setEmail(email);
//	}
	
	

	public User(String username, String password) {

		super(User.class, username, password);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Tale> getTales() {
		return tales;
	}

	public void setTales(List<Tale> tales) {
		this.tales = tales;
	}

	public Dummy[] getDummies() {
		return dummies;
	}

	public void setDummies(Dummy[] dummies) {
		this.dummies = dummies;
	}
	
	
	
	
}