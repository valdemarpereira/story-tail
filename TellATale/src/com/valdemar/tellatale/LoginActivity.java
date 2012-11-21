package com.valdemar.tellatale;

import com.valdemar.tellatale.services.LoginService;
import com.valdemar.tellatale.services.LoginService.OnLoginListner;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
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

		setTitle(R.string.app_name);
		login = (Button) findViewById(R.id.login_next_button);
		forgetPwd = (Button) findViewById(R.id.password_forgot_button);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		staySignIn = (CheckBox) findViewById(R.id.keep_logged);

		login.setOnClickListener(onLoginClick);
		password.setOnEditorActionListener(onPasswordEditorActionListener);

		setSupportProgressBarIndeterminateVisibility(false);

	}

	OnClickListener onLoginClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			hideSoftKeyboard(v);
			doLogin();
		}

	};



	OnEditorActionListener onPasswordEditorActionListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_GO) { // depends what you set in imeOtions in layout
				
				hideSoftKeyboard(v);

				doLogin();
				return true;
			}
			return false;
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

		setLoadingFrameVisibility(View.VISIBLE,R.string.lbl_loading_signing_in);

		LoginService login = new LoginService(new OnLoginListner() {

			@Override
			public void onSuccess() {
				Intent intent = new Intent(getBaseContext(), TalesActivity.class);
				if (staySignIn.isChecked()) {
					mAppContext.storeCredentials(username.getText().toString(), password.getText().toString());
				}

				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure() {
				runOnUiThread(new Runnable() {

					public void run() {
						Toast.makeText(getBaseContext(), R.string.lbl_invalid_login_credentials, Toast.LENGTH_SHORT).show();

						// setSupportProgressBarIndeterminateVisibility(false);
						setLoadingFrameVisibility(View.GONE);
					}
				});
			}
		});


		login.doLogin(username.getText().toString().trim(), password.getText().toString().trim());

	}


	private boolean validateLogin() {

		if (!mAppContext.isNetworkAvailable()) {
			Toast.makeText(this, R.string.lbl_network_unavailable, Toast.LENGTH_SHORT).show();
			return false;
		}

		if (username.getText().toString().trim().length() == 0 || password.getText().toString().trim().length() == 0) {
			Toast.makeText(this, R.string.lbl_empty_login_credentials, Toast.LENGTH_SHORT).show();
			return false;
		} else
			return true;
	}

}
