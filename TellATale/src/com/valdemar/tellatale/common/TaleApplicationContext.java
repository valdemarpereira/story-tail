package com.valdemar.tellatale.common;

import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.api.StackMob;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Pair;

public class TaleApplicationContext extends Application {


	private static final String API_KEY_DEV = "2bff1eb8-40ed-4dc7-84be-cd26f78b0ef2";
	//private static final String API_KEY_PROD = "d57495eb-5dcb-41a4-8092-d8765a282f2d";

	private static final String MY_PREFS_FILE_NAME = "TalePrefFile";
	private static final String ARG1_KEY = "arg1";
	private static final String ARG2_KEY = "arg2";

	private Typeface angelina;
	private Typeface handsean;

	@Override
	public void onCreate() {
		super.onCreate();

		StackMobAndroid.init(getApplicationContext(), 0,
				API_KEY_DEV);

		StackMob.getStackMob().getSession().getLogger().setLogging(true); 

		angelina = Typeface.createFromAsset(getAssets(), "fonts/angelina.ttf");
		handsean = Typeface.createFromAsset(getAssets(), "fonts/handsean.ttf");
	}

	public Typeface getStoryFont() {
		return angelina;

	}

	public Typeface getStoryFontHandseam() {
		return handsean;
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	public void storeCredentials(String username, String password) {

		final SharedPreferences prefs = new ObscuredSharedPreferences(this,
				this.getSharedPreferences(MY_PREFS_FILE_NAME,
						Context.MODE_PRIVATE));

		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(ARG1_KEY, username);
		editor.putString(ARG2_KEY, password);
		editor.commit();

	}

	public void cleanCredentials() {
		final SharedPreferences prefs = new ObscuredSharedPreferences(this,
				this.getSharedPreferences(MY_PREFS_FILE_NAME,
						Context.MODE_PRIVATE));
		SharedPreferences.Editor editor = prefs.edit();

		editor.remove(ARG1_KEY);
		editor.remove(ARG2_KEY);
		editor.commit();
	}

	public boolean hasStoredCredentials() {
		Pair<String, String> c = getStoredCredentials();

		if (c.first.length() > 0 && c.second.length() > 0)
			return true;
		else
			return false;

	}

	public Pair<String, String> getStoredCredentials() {
		final SharedPreferences prefs = new ObscuredSharedPreferences(this,
				this.getSharedPreferences(MY_PREFS_FILE_NAME,
						Context.MODE_PRIVATE));

		String arg1 = prefs.getString(ARG1_KEY, "");
		String arg2 = prefs.getString(ARG2_KEY, "");

		return new Pair<String, String>(arg1, arg2);
	}
}
