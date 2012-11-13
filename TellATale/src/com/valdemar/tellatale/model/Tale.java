package com.valdemar.tellatale.model;

import com.stackmob.sdk.api.StackMobGeoPoint;
import com.stackmob.sdk.model.StackMobModel;

public class Tale extends StackMobModel {
	
	private StackMobGeoPoint currentcoord;
	private boolean locked;
	private String currentcity;
	private String currentcountry;
	private long currentdistance;
	private long currentinteraction;
	private String language;
	private long lock_date_utc;
	private long maxdays;
	private long maxinteractions;
	private String title;

	
//	private String createduser;
//	private String tale_id;
//	private long lastmoddate;
//	private long createddate;

	public Tale() {

		super(Tale.class);
		// this.name = name;
	}

//	public String getCreateduser() {
//		return createduser;
//	}
//
//	public void setCreateduser(String createduser) {
//		this.createduser = createduser;
//	}

	public StackMobGeoPoint getCurrentcoord() {
		return currentcoord;
	}

	public void setCurrentcoord(StackMobGeoPoint currentcoord) {
		this.currentcoord = currentcoord;
	}

	public String getCurrentcity() {
		return currentcity;
	}

	public void setCurrentcity(String currentcity) {
		this.currentcity = currentcity;
	}

	public boolean getLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

//	public String getTale_id() {
//		return tale_id;
//	}
//
//	public void setTale_id(String tale_id) {
//		this.tale_id = tale_id;
//	}
//
//	public long getLastmoddate() {
//		return lastmoddate;
//	}
//
//	public void setLastmoddate(long lastmoddate) {
//		this.lastmoddate = lastmoddate;
//	}

	public long getMaxdays() {
		return maxdays;
	}

	public void setMaxdays(long maxdays) {
		this.maxdays = maxdays;
	}

	public long getMaxinteractions() {
		return maxinteractions;
	}

	public void setMaxinteractions(long maxinteractions) {
		this.maxinteractions = maxinteractions;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public long getCurrentinteraction() {
		return currentinteraction;
	}

	public void setCurrentinteraction(long currentinteraction) {
		this.currentinteraction = currentinteraction;
	}

//	public long getCreateddate() {
//		return createddate;
//	}
//
//	public void setCreateddate(long createddate) {
//		this.createddate = createddate;
//	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCurrentcountry() {
		return currentcountry;
	}

	public void setCurrentcountry(String currentcountry) {
		this.currentcountry = currentcountry;
	}

	public long getCurrentdistance() {
		return currentdistance;
	}

	public void setCurrentdistance(long currentdistance) {
		this.currentdistance = currentdistance;
	}

	public long getLock_date_utc() {
		return lock_date_utc;
	}

	public void setLock_date_utc(long lock_date_utc) {
		this.lock_date_utc = lock_date_utc;
	}

}
