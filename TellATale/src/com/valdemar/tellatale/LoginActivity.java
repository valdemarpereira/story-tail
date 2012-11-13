package com.valdemar.tellatale;

import com.valdemar.tellatale.services.LoginService;
import com.valdemar.tellatale.services.LoginService.OnLoginListner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends TaleBaseActivity {
	private Button login, forgetPwd;
	private EditText username, password;
	private CheckBox staySignIn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_layout);

		login = (Button) findViewById(R.id.login_next_button);
		forgetPwd = (Button) findViewById(R.id.password_forgot_button);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		staySignIn = (CheckBox) findViewById(R.id.keep_logged);
		
		login.setOnClickListener(onLoginClick);

		setSupportProgressBarIndeterminateVisibility(false);

	}

	OnClickListener onLoginClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			doLogin();
		}

	};

	OnClickListener onSignupClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

	private void doLogin() {
		
		if (!validateLogin())
			return;

		setLoadingFrameVisibility(View.VISIBLE);
		
		
		LoginService login = new LoginService(new OnLoginListner() {
			
			@Override
			public void onSuccess() {
				Intent intent = new Intent(getBaseContext(),
						TalesActivity.class);
				if(staySignIn.isChecked())
				{
					mAppContext.storeCredentials(username.getText().toString(), password.getText().toString());
				}
				
				//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();					
			}
			
			@Override
			public void onFailure() {
				runOnUiThread(new Runnable() {
					public void run() {

						Toast.makeText(getBaseContext(),
								R.string.lbl_invalid_login_credentials,
								Toast.LENGTH_SHORT).show();

						//setSupportProgressBarIndeterminateVisibility(false);
						setLoadingFrameVisibility(View.GONE);
					}
				});
				
			}
		});
		
		
		login.doLogin(username.getText().toString().trim(), password.getText().toString().trim());
		/*
		TaleUser user = new TaleUser(username.getText().toString(), password.getText().toString());
		user.login(new StackMobCallback() {
			
			@Override
			public void success(String arg0) {
				Intent intent = new Intent(getBaseContext(),
						TalesActivity.class);
				if(staySignIn.isChecked())
				{
					mAppContext.storeCredentials(username.getText().toString(), password.getText().toString());
				}
				
				//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();				
			}
			
			@Override
			public void failure(StackMobException arg0) {
				runOnUiThread(new Runnable() {
					public void run() {

						Toast.makeText(getBaseContext(),
								R.string.lbl_invalid_login_credentials,
								Toast.LENGTH_SHORT).show();

						//setSupportProgressBarIndeterminateVisibility(false);
						setLoadingFrameVisibility(View.GONE);
					}
				});
				
			}
		});
		*/
		/*
		
		LoginService login = new LoginService(new OnLoginListner() {
			
			@Override
			public void onSuccess() {
				Intent intent = new Intent(getBaseContext(),
						TalesActivity.class);
				
				if(staySignIn.isChecked())
				{
					mAppContext.storeCredentials(username.getText().toString(), password.getText().toString());
				}
				
				//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				
			}
			
			@Override
			public void onFailure() {
				runOnUiThread(new Runnable() {
					public void run() {

						Toast.makeText(getBaseContext(),
								R.string.lbl_invalid_login_credentials,
								Toast.LENGTH_SHORT).show();

						//setSupportProgressBarIndeterminateVisibility(false);
						setLoadingFrameVisibility(View.GONE);
					}
				});

			}
				
		});
		
		login.doLogin(username.getText().toString(), password.getText().toString());
*/

//		Map<String, String> args = new HashMap<String, String>();
//		args.put("username", username.getText().toString());
//		args.put("password", password.getText().toString());
//		StackMobCommon.getStackMobInstance().login(args,
//				new StackMobCallback() {
//					@Override
//					public void success(String response) {
//						
//					}
//
//					@Override
//					public void failure(StackMobException e) {
//
//						
//				});

	}

//	private void setEnableControls(boolean enable) {
//		username.setEnabled(enable);
//		password.setEnabled(enable);
//		forgetPwd.setEnabled(enable);
//		login.setEnabled(enable);
//	}

	private boolean validateLogin() {
		
		if (!mAppContext.isNetworkAvailable())
		{
			Toast.makeText(this, R.string.lbl_network_unavailable,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if (username.getText().toString().trim().length() == 0
				|| password.getText().toString().trim().length() == 0) {
			Toast.makeText(this, R.string.lbl_empty_login_credentials,
					Toast.LENGTH_SHORT).show();
			return false;
		} else
			return true;
	}

}