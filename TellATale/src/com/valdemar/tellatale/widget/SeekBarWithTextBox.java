package com.valdemar.tellatale.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.valdemar.tellatale.R;

public class SeekBarWithTextBox extends LinearLayout {

	public interface OnSeekBarWithTextBoxChangeValueListner {
		public void onProgressChanged(SeekBar seekBar, TextView subTitle, int progress);
	}

	private TextView subTitle;
	private SeekBar seekBar;
	private OnSeekBarWithTextBoxChangeValueListner onSeekBarWithTextBoxChangeValueListner;

	public SeekBarWithTextBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(VERTICAL);
		setGravity(Gravity.CENTER);

		LayoutInflater.from(context).inflate(R.layout.seek_bar_with_textbox, this, true);

		seekBar = (SeekBar) findViewById(R.id.seekbar);
		subTitle = (TextView) findViewById(R.id.sub_title);

		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SeekBarWithTextBox, 0, 0);
		//
		String text = array.getString(R.styleable.SeekBarWithTextBox_seekbar_title);
		if (text == null)
			text = "";
		((TextView) findViewById(R.id.title)).setText(text);

		int max = array.getInt(R.styleable.SeekBarWithTextBox_seekbar_max, 99);
		seekBar.setMax(max);

		array.recycle();

		seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
	}

	public int getSeekBarValue() {
		return seekBar.getProgress();
	}

	public OnSeekBarWithTextBoxChangeValueListner getOnSeekBarWithTextBoxChangeValueListner() {
		return onSeekBarWithTextBoxChangeValueListner;
	}

	public void setOnSeekBarWithTextBoxChangeValueListner(OnSeekBarWithTextBoxChangeValueListner onSeekBarWithTextBoxChangeValueListner) {
		this.onSeekBarWithTextBoxChangeValueListner = onSeekBarWithTextBoxChangeValueListner;
	}

	OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (onSeekBarWithTextBoxChangeValueListner != null)
				onSeekBarWithTextBoxChangeValueListner.onProgressChanged(seekBar, subTitle, progress);
		}
	};

	public void setInitialValue(int value, CharSequence charSequence) {
		seekBar.setProgress(value);
		subTitle.setText(charSequence);
	}
}
