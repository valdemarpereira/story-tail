package com.valdemar.tellatale;

import com.stackmob.sdk.api.StackMobGeoPoint;
import com.valdemar.tellatale.model.GPSCoord;
import com.valdemar.tellatale.model.Tail;
import com.valdemar.tellatale.model.Tale;
import com.valdemar.tellatale.services.LoginService;
import com.valdemar.tellatale.services.StartTaleService;
import com.valdemar.tellatale.services.StartTaleService.OnStartTaleListner;
import com.valdemar.tellatale.widget.SeekBarWithTextBox;
import com.valdemar.tellatale.widget.SeekBarWithTextBox.OnSeekBarWithTextBoxChangeValueListner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class StartTaleActivity extends TaleBaseActivity {

	protected static final String LOCATION_KEY = "LOCATION";
	protected static final String CACHE_TALE_TITLE = "CACHE_TALE_TITLE";
	protected static final String CACHE_TALE_STORY = "CACHE_TALE_STORY";
	private static final String CACHE_TALE_USERNAME = "CACHE_TALE_USERNAME";

	private SeekBarWithTextBox seekBarMaxDays, seekBarMaxInteractions;
	private Button addTale;
	private Resources res;
	private EditText title, story;
	private Location location;
	private SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_tale_layout);
		res = getResources();

		title = (EditText) findViewById(R.id.title);
		title.addTextChangedListener(onTitleTextWatcher);

		story = (EditText) findViewById(R.id.story);
		story.addTextChangedListener(onStoryTextWatcher);

		seekBarMaxDays = (SeekBarWithTextBox) findViewById(R.id.seekbar_maxdays);
		seekBarMaxDays.setOnSeekBarWithTextBoxChangeValueListner(onSeekBarMaxDaysChangeValueListner);
		seekBarMaxDays.setInitialValue(0, res.getQuantityString(R.plurals.lbl_add_tale_day, 3, 3));

		seekBarMaxInteractions = (SeekBarWithTextBox) findViewById(R.id.seekbar_max_interactions);
		seekBarMaxInteractions.setOnSeekBarWithTextBoxChangeValueListner(onSeekBarMaxInterationsChangeValueListner);
		seekBarMaxInteractions.setInitialValue(0, res.getQuantityString(R.plurals.lbl_add_tale_interactions, 5, 5));

		addTale = (Button) findViewById(R.id.add_tale);
		addTale.setOnClickListener(onAddTaleClickListener);

		prefs = getSharedPreferences("STORY_CACHE", MODE_PRIVATE);
		init();
	}

	private void init() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			this.location = extras.getParcelable(LOCATION_KEY);
		}

		// check if the tale cache bellongs to the current user

		if (!(prefs.getString(CACHE_TALE_USERNAME, "").equals(LoginService.user.getUsername()))) {
			cleanCacheStory();

		} else if (prefs.contains(CACHE_TALE_TITLE) || prefs.contains(CACHE_TALE_STORY)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.lbl_previous_cache_tale).setCancelable(false).setPositiveButton(R.string.lbl_continue_tale, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

					loadLastTale();

				}

			}).setNegativeButton(R.string.lbl_start_new_tale, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
					cleanCacheStory();

				}
			}).show();
		}
		
		//set username
		saveCacheStory(CACHE_TALE_USERNAME, LoginService.user.getUsername());
	}

	private void loadLastTale() {

		if (prefs.contains(CACHE_TALE_TITLE))
			title.setText(prefs.getString(CACHE_TALE_TITLE, ""));
		if (prefs.contains(CACHE_TALE_STORY))
			story.setText(prefs.getString(CACHE_TALE_STORY, ""));
	}

	OnClickListener onAddTaleClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Pair<Tale, Tail> tale = validateAndGetTale();
			if (tale == null)
				return;

			StartTaleService startTale = new StartTaleService(tale.first, tale.second, onStartTaleListner);

			setLoadingFrameVisibility(View.VISIBLE, R.string.lbl_creating_tale);
			startTale.doStartATale();
		}

	};

	OnStartTaleListner onStartTaleListner = new OnStartTaleListner() {

		@Override
		public void onSuccess() {

			runOnUiThread(new Runnable() {
				public void run() {
					setLoadingFrameVisibility(View.GONE);

					Toast.makeText(getBaseContext(), R.string.lbl_new_tale_added, Toast.LENGTH_SHORT).show();
					cleanCacheStory();
					finish();

				}
			});

		}

		@Override
		public void onFailureOnAddTail() {

			runOnUiThread(new Runnable() {
				public void run() {
					setLoadingFrameVisibility(View.GONE);

					Toast.makeText(getBaseContext(), R.string.lbl_new_tale_error, Toast.LENGTH_SHORT).show();

				}
			});

		}

		@Override
		public void onFailure() {

			runOnUiThread(new Runnable() {
				public void run() {
					setLoadingFrameVisibility(View.GONE);

					Toast.makeText(getBaseContext(), R.string.lbl_new_tale_error, Toast.LENGTH_SHORT).show();

				}
			});
		}
	};

	private void cleanCacheStory() {
		Editor edit = prefs.edit();
		edit.clear();
		edit.commit();

	}

	private void saveCacheStory(String key, String value) {
		Editor edit = prefs.edit();

		edit.putString(key, value);
		edit.commit();
	}

	private Pair<Tale, Tail> validateAndGetTale() {

		String title_t = title.getEditableText().toString();
		title_t = title_t.trim();

		String story_t = story.getEditableText().toString();
		story_t = story_t.trim();

		if (title_t.length() == 0) {
			Toast.makeText(getBaseContext(), R.string.lbl_title_is_empty, Toast.LENGTH_SHORT).show();
			return null;
		}

		if (story_t.length() < 10) {
			Toast.makeText(getBaseContext(), R.string.lbl_tale_too_short, Toast.LENGTH_SHORT).show();
			return null;
		}

		Tale tale = new Tale();
		// tale.setCurrentcoord(new GPSCoord(this.location));

		tale.setCurrentcoord(new StackMobGeoPoint(this.location.getLongitude(), this.location.getLatitude()));

		// tale.setCurrentcountry()
		tale.setCurrentdistance(0l);
		tale.setCurrentinteraction(1l);
		// tale.setCreateduser(getTaleAppContext().getUserName())
		// tale.setLanguage(language)
		tale.setLocked(false);
		tale.setMaxdays(getUserMaxDays());
		tale.setMaxinteractions(getUserMaxInteractions());
		tale.setTitle(title_t);

		Tail tail = new Tail();
		// tail.setCity(city);
		tail.setCoord(new GPSCoord(this.location));
		// tail.setCountry(country);
		// tail.setCreatedby(getTaleAppContext().getUserName());
		tail.setStory(story_t);

		return new Pair<Tale, Tail>(tale, tail);

	}

	private Long getUserMaxInteractions() {
		int value = seekBarMaxInteractions.getSeekBarValue();
		return value + 5l;
	}

	private Long getUserMaxDays() {
		int value = seekBarMaxDays.getSeekBarValue();

		value += 3;

		if (value <= 6) {
			// do nothing;
		} else if (value <= 9) {
			value = value - 6;
			value = value * 7;
		} else {
			value = value - 9;
			value = value * 30;
		}

		return (long) value;

	}

	TextWatcher onTitleTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			setTitle(s);
			saveCacheStory(CACHE_TALE_TITLE, s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	TextWatcher onStoryTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			saveCacheStory(CACHE_TALE_STORY, s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	OnSeekBarWithTextBoxChangeValueListner onSeekBarMaxDaysChangeValueListner = new OnSeekBarWithTextBoxChangeValueListner() {

		@Override
		public void onProgressChanged(SeekBar seekBar, TextView subTitle, int progress) {

			progress += 3;

			if (progress <= 6) {
				subTitle.setText(res.getQuantityString(R.plurals.lbl_add_tale_day, progress, progress));
			} else if (progress <= 9) {
				subTitle.setText(res.getQuantityString(R.plurals.lbl_add_tale_week, progress - 6, progress - 6));
			} else {
					subTitle.setText(res.getQuantityString(R.plurals.lbl_add_tale_month, progress - 9, progress - 9));

				}

		}
	};

	OnSeekBarWithTextBoxChangeValueListner onSeekBarMaxInterationsChangeValueListner = new OnSeekBarWithTextBoxChangeValueListner() {

		@Override
		public void onProgressChanged(SeekBar seekBar, TextView subTitle, int progress) {

			progress += 5;

			subTitle.setText(res.getQuantityString(R.plurals.lbl_add_tale_interactions, progress, progress));

		}
	};

}
