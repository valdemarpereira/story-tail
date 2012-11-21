package com.valdemar.tellatale.services;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.valdemar.tellatale.model.User;

public final class LoginService {

	public static User user;

	public interface OnLoginListner {
		public void onSuccess();

		public void onFailure();
	}

	private OnLoginListner onLoginListner;

	public void setOnLoginListener(OnLoginListner onLoginListner) {
		this.onLoginListner = onLoginListner;
	}

	public LoginService(OnLoginListner onLoginListner) {
		setOnLoginListener(onLoginListner);
	}

	public void doLogin(String username, String password) {

		user = new User(username, password);
		user.login(StackMobOptions.none(), new StackMobCallback() {

			@Override
			public void failure(StackMobException arg0) {
				onLoginListner.onFailure();
			}

			@Override
			public void success(String arg0) {
				onLoginListner.onSuccess();
			}

		});
	}

}
