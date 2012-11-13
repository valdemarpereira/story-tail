package com.valdemar.tellatale;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Window;
import com.valdemar.tellatale.common.TaleApplicationContext;

public class TaleBaseMapActivity  extends SherlockMapActivity {

	private LinearLayout baseLayout;
	private View loadingFrame;
	protected TaleApplicationContext mAppContext;
	private TextView loadingText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		mAppContext = (TaleApplicationContext) getApplicationContext();
		
		super.setContentView(R.layout.base_activity_layout);
		
		baseLayout = (LinearLayout) findViewById(R.id.root_container);
		setSupportProgressBarIndeterminateVisibility(false);

		
	}

	
	@Override
	public void setContentView(int layoutResID)
	{
		FrameLayout fl = (FrameLayout) getLayoutInflater().inflate(R.layout.generic_loading_frame, null);
		FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		fl.setLayoutParams(flp);
		
		loadingFrame = fl.findViewById(R.id.loading_frame);
		loadingText = (TextView) loadingFrame.findViewById(R.id.pending_text);
		
		View view = (View) getLayoutInflater().inflate(layoutResID, null);
		
		LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f);
		view.setLayoutParams(lp);
		
		fl.addView(view, 0);

		baseLayout.addView(fl);
		
		setLoadingFrameVisibility(View.GONE);
	}
	

	protected TaleApplicationContext getTaleAppContext()
	{
		return mAppContext;
	}

	@Override
	public void setTitle(int textRes)
	{
		getSupportActionBar().setTitle(textRes);
	}

	@Override
	public void setTitle(CharSequence title)
	{
		getSupportActionBar().setTitle(title);
	}
	
	public void setTitle(String title)
	{
		getSupportActionBar().setTitle(title);
	}
	
	
	public void hideActionBar()
	{
		getSupportActionBar().hide();
	}
	
	public void showActionBar()
	{
		getSupportActionBar().show();
	}
	
	
	protected void setLoadingFrameVisibility(int visibility)
	{
		loadingFrame.setVisibility(visibility);
		loadingText.setText(R.string.progress_container_text);

	}

	protected void setLoadingFrameVisibility(int visible, int resId) {
		loadingFrame.setVisibility(visible);
		loadingText.setText(resId);
	}
	
	protected OnClickListener onBackButtonPressed = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			onBackPressed();
		}
	};

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//	    switch (item.getItemId()) {
//	        case android.R.id.home:
//	        	onBackPressed();
//	            return true;
//	        default:
//	            return super.onOptionsItemSelected(item);
//	    }
//	}
}
