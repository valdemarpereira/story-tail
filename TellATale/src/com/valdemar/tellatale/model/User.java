package com.valdemar.tellatale.model;

import java.util.ArrayList;
import java.util.List;

import com.stackmob.sdk.model.StackMobUser;

public class User extends StackMobUser {
	private String email;
	private List<Tale> tales = new ArrayList<Tale>();


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

	
}