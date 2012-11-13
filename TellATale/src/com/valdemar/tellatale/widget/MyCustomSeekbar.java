package com.valdemar.tellatale.widget;

import com.valdemar.tellatale.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MyCustomSeekbar extends LinearLayout {

	private TextView tip;
	private View tooltip;
	private SeekBar seekbar;

	public MyCustomSeekbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.seekbar_with_tooltip, this);
		init();
	}

	public MyCustomSeekbar(Context context) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.seekbar_with_tooltip, this);

		init();

	}

	private void init() {
		tooltip = (View) findViewById(R.id.tooltip);
		tip = (TextView) tooltip.findViewById(R.id.tip);
		seekbar = (SeekBar) findViewById(R.id.seekbar);

		seekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);

	}

	public void setProgress(int pos) {
		seekbar.setProgress(pos);

	}

	OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

			
			int xPos = (((seekbar.getRight() - seekbar.getLeft()) * seekbar.getProgress()) /
		            seekbar.getMax()) + seekbar.getLeft();
			 tip.setText(Integer.toString(xPos));
			 
			 
			 LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tooltip.getLayoutParams();
				params.setMargins(xPos, 0, 0, 0); //substitute parameters for left, top, right, bottom
				
				
			 tooltip.setLayoutParams(params);
			// tip.setText(seekBar.get);

		}
	};

}
