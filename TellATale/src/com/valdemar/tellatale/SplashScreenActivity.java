package com.valdemar.tellatale;

import com.valdemar.tellatale.common.TaleApplicationContext;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class SplashScreenActivity extends Activity
{
	protected int _splashTime = 2000;
	Thread splashTread;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		TaleApplicationContext appContext = (TaleApplicationContext) getApplicationContext();
//		TextView title = (TextView) findViewById(R.id.welcome_catchphrase);
		
//		title.setTypeface(appContext.getStoryFontHandseam());
		//title.setTextSize(18);
//		title.setText("Tell a Tale");
	    
	    
		// thread for displaying the SplashScreen
		splashTread = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					int waited = 0;
					while (waited < _splashTime)
					{
						sleep(100);
						waited += 100;
					}
				}
				catch (InterruptedException e)
				{
					return;
				}

				startActivity(new Intent(getBaseContext(), TalesActivity.class));
				finish();
			}
		};
		splashTread.start();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		if (splashTread != null)
		{
			splashTread.interrupt();
		}
	}

}