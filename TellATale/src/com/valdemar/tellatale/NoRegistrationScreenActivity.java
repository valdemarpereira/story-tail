package com.valdemar.tellatale;

import java.util.List;

import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.valdemar.tellatale.common.TaleApplicationContext;
import com.valdemar.tellatale.model.Dummy;
import com.valdemar.tellatale.model.Tale;
import com.valdemar.tellatale.services.LoginService;
import com.valdemar.tellatale.services.LoginService.OnLoginListner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NoRegistrationScreenActivity extends TaleBaseActivity {
	private Button login, signup, stackmob;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		hideActionBar();
		setContentView(R.layout.no_registration_screen);

		TaleApplicationContext appContext = (TaleApplicationContext) getApplicationContext();

		login = (Button) findViewById(R.id.login);
		signup = (Button) findViewById(R.id.signup);
		stackmob = (Button) findViewById(R.id.stackmob);
		
		login.setOnClickListener(onLoginClick);
		signup.setOnClickListener(onSignupClick);
		stackmob.setOnClickListener(onStackmobClick);
		
		
		
		if (mAppContext.hasStoredCredentials()) {
			doSilentLogin();
		}

	}

	private void doSilentLogin() {
		login.setVisibility(View.GONE);
		signup.setVisibility(View.GONE);
		
		if (!mAppContext.isNetworkAvailable())
		{
			Toast.makeText(this, R.string.lbl_network_unavailable,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		setLoadingFrameVisibility(View.VISIBLE, R.string.lbl_silent_login);


		LoginService login = new LoginService(new OnLoginListner() {

			@Override
			public void onSuccess() {
				Intent intent = new Intent(getBaseContext(),
						TalesActivity.class);

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

						// setSupportProgressBarIndeterminateVisibility(false);
						setLoadingFrameVisibility(View.GONE);
					}
				});

			}

		});

		login.doLogin(mAppContext.getStoredCredentials().first,
				mAppContext.getStoredCredentials().second);

	}

	

	OnClickListener onLoginClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getBaseContext(), LoginActivity.class);
			startActivity(intent);
			finish();
		}
	};
	OnClickListener onSignupClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	
	/* Stackmob:
	 * 
	 * Trying to insert a ne Dummy object, related to the user object
	 * The Dummy Object has a property called Title. 
	 * I always set this to "Teste 1 - Valdemar"
	 * 
	 * I always get a successful response, but in the stackmob dashboard, i get a new Dummy object with the title as null...
	 * 
	 * 
	 *  */
	OnClickListener onStackmobClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			
			Dummy dummy = new Dummy("Valdemar 101010");
			
			dummy.save(new StackMobOptions().withDepthOf(1));
			
			
			Dummy.query(Dummy.class, new StackMobQuery().isInRange(0, 9), new StackMobQueryCallback<Dummy>() {
			    @Override
			    public void success(List<Dummy> result) {
			       // You've now got a list of all tasks
			    	
			    	String xpto = "22";
			    }
			 
			    @Override
			    public void failure(StackMobException e) {
			    	String xpto = "22";
			    }
			});
			
			
			
			
			/*
			
			
			final Dummy dummy = new Dummy("Test 3 - Valdemar");

	        StackMob.getLogger().setLogging(true);
			
	        final User user = new User("jonh", null);
			
	        
	        user.fetchWithDepth(2, new StackMobCallback() {
	            @Override
	            public void success(String responseBody) {
	                StackMob.getLogger().logInfo("Response body when fetching: " + responseBody);

	                // append to user.dummies array
	                Dummy[] newDummies = new Dummy[user.dummies.length+1];
	                System.arraycopy(user.dummies, 0, newDummies, 0, user.dummies.length);
	                newDummies[newDummies.length-1] = dummy;
	                user.dummies = newDummies;

	                user.saveWithDepth(2, new StackMobCallback() {
	                    @Override
	                    public void success(String responseBody) {
	                        StackMob.getLogger().logInfo("Got response body after saving: " + responseBody);
	                    }

	                    @Override
	                    public void failure(StackMobException e) {
	                        StackMob.getLogger().logWarning("Saving failed");
	                    }
	                });
	            }

	            @Override
	            public void failure(StackMobException e) {
	                StackMob.getLogger().logWarning("Fetching information failed");
	            }
	        });
			
			
			
			*/
//			
//			Dummy dummy;
//			dummy = new Dummy("Teste 1 - Valdemar");
//
//			StackMob stackmob = StackMobCommon.getStackMobInstance();
//			stackmob.postRelated("user", "jonh", "dummies", dummy.toJson(), new StackMobCallback() {
//				
//				@Override
//				public void success(String arg0) {
//					
//					runOnUiThread(new Runnable() {
//						public void run() {
//
//							Toast.makeText(getBaseContext(),
//									"Dummy Object Inserted.",
//									Toast.LENGTH_SHORT).show();
//						}
//					});
//					
//				}
//				
//				@Override
//				public void failure(StackMobException arg0) {
//					
//					runOnUiThread(new Runnable() {
//						public void run() {
//
//							Toast.makeText(getBaseContext(),
//									"Error inserting the Dummy Object.",
//									Toast.LENGTH_SHORT).show();	
//						}
//					});
//					
//				}
//			});
//		}
		}
	};

}