package com.valdemar.tellatale.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;

import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.exception.StackMobHTTPResponseException;
import com.valdemar.tellatale.model.User;

public class RegisterService {

	public interface OnRegisterListner {
		public void onSuccess();

		public void onFailure();

		public void usernameAlreadyRegisterd();
	}

	private OnRegisterListner onRegisterListner;

	public void setOnRegisterListner(OnRegisterListner onRegisterListner) {
		this.onRegisterListner = onRegisterListner;
	}

	public RegisterService(OnRegisterListner onRegisterListner) {
		setOnRegisterListner(onRegisterListner);
	}

	public void doRegister(String username, String password) {
		// TODO: VP: Testar reft
		User user = new User(username, password);
		user.save(new StackMobCallback() {

			@Override
			public void success(String arg0) {
				onRegisterListner.onSuccess();
			}

			@Override
			public void failure(StackMobException e) {
				if (e instanceof StackMobHTTPResponseException) {
					StackMobHTTPResponseException exc = (StackMobHTTPResponseException) e;

					if (exc.getCode() == HttpStatus.SC_CONFLICT)
						onRegisterListner.usernameAlreadyRegisterd();
				} else
					onRegisterListner.onFailure();
			}
		});

		/*
		 * Map<String, String> user = new HashMap<String, String>();
		 * user.put("username", username); user.put("password", password);
		 * StackMob stackmob = StackMobCommon.getStackMobInstance();
		 * stackmob.post("user", user, new StackMobCallback() {
		 * 
		 * @Override public void success(String response) {
		 * onRegisterListner.onSuccess(); }
		 * 
		 * @Override public void failure(StackMobException e) {
		 * 
		 * if (e instanceof StackMobHTTPResponseException) {
		 * StackMobHTTPResponseException exc = (StackMobHTTPResponseException)
		 * e;
		 * 
		 * if (exc.getCode() == HttpStatus.SC_CONFLICT)
		 * onRegisterListner.usernameAlreadyRegisterd(); } else
		 * onRegisterListner.onFailure();
		 * 
		 * } });
		 */

	}

}
