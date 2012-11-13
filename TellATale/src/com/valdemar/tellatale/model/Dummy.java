package com.valdemar.tellatale.model;

import com.stackmob.sdk.model.StackMobModel;

public class Dummy extends StackMobModel {

	private String title;

	public Dummy() {
		super(Dummy.class);
	}

	public Dummy(String title) {
		super(Dummy.class);
		this.setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
