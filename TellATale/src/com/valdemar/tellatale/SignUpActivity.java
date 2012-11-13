package com.valdemar.tellatale;

import com.valdemar.tellatale.services.LoginService;
import com.valdemar.tellatale.services.LoginService.OnLoginListner;
import com.valdemar.tellatale.services.RegisterService.OnRegisterListner;
import com.valdemar.tellatale.services.RegisterService;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends TaleBaseActivity {
	private Button register;
	private EditText username, name, email, password, passwordConf;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up_layout);

		register = (Button) findViewById(R.id.register);
		username = (EditText) findViewById(R.id.username);
		name = (EditText) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.info_password);
		passwordConf = (EditText) findViewById(R.id.info_password_confirmation);
		register.setOnClickListener(onRegisterClick);

	}

	OnClickListener onRegisterClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			doRegister();
		}
	};

	private void doRegister() {

		if (!validateRegister())
			return;
		// TODO: Guardar o email

		setLoadingFrameVisibility(View.VISIBLE, R.string.lbl_reg_server_wait);

		RegisterService login = new RegisterService(new OnRegisterListner() {

			@Override
			public void onSuccess() {

				runOnUiThread(new Runnable() {
					public void run() {

						// do a silent login
						LoginService login = new LoginService(new OnLoginListner() {
							@Override
							public void onSuccess() {

								runOnUiThread(new Runnable() {
									public void run() {

										finalizeRegProcess();
									}
								});
							}

							@Override
							public void onFailure() {

								runOnUiThread(new Runnable() {
									public void run() {

										Toast.makeText(getBaseContext(), "Error while loging in... please try to loging again...", Toast.LENGTH_SHORT).show();
									}
								});

							}
						});

						login.doLogin(username.getEditableText().toString().trim(), password.getEditableText().toString().trim());
					}
				});

			}

			private void finalizeRegProcess() {

				setLoadingFrameVisibility(View.GONE);

				AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
				builder.setMessage(R.string.lbl_keep_user_logged).setCancelable(false).setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						mAppContext.storeCredentials(username.getText().toString().trim(), password.getText().toString().trim());
						dialog.dismiss();
						Intent intent = new Intent(getBaseContext(), TalesActivity.class);
						startActivity(intent);
						finish();
					}
				}).setNegativeButton(R.string.lbl_no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						Intent intent = new Intent(getBaseContext(), TalesActivity.class);
						startActivity(intent);
						finish();
					}
				});

				AlertDialog alert = builder.create();
				alert.show();

			}

			@Override
			public void onFailure() {
				runOnUiThread(new Runnable() {
					public void run() {

						Toast.makeText(getBaseContext(), R.string.lbl_reg_failed, Toast.LENGTH_SHORT).show();
						setLoadingFrameVisibility(View.GONE);
					}
				});

			}

			@Override
			public void usernameAlreadyRegisterd() {

				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getBaseContext(), R.string.lbl_reg_username_already_registerd, Toast.LENGTH_SHORT).show();
						setLoadingFrameVisibility(View.GONE);
					}
				});

			}
		});

		login.doRegister(username.getText().toString().trim(), password.getText().toString().trim());
	}

	private boolean validateRegister() {

		if (!mAppContext.isNetworkAvailable()) {
			Toast.makeText(this, R.string.lbl_network_unavailable, Toast.LENGTH_SHORT).show();
			return false;
		}

		if (username.getText().toString().trim().length() == 0 || password.getText().toString().trim().length() == 0) {
			Toast.makeText(this, R.string.lbl_empty_reg_fields, Toast.LENGTH_SHORT).show();
			return false;
		} else if (!password.getText().toString().trim().equals(passwordConf.getText().toString().trim())) {
			Toast.makeText(this, R.string.lbl_reg_mismatch_password, Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	OnClickListener onSignupClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

}