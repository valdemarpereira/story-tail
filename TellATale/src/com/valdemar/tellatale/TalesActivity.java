package com.valdemar.tellatale;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.stackmob.sdk.api.StackMobGeoPoint;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.valdemar.tellatale.map.CircleOverlay;
import com.valdemar.tellatale.map.MyOverlay;
import com.valdemar.tellatale.map.StoryTailOverlay;
import com.valdemar.tellatale.map.StoryTailOverlay.OnItemClickEvent;
import com.valdemar.tellatale.map.StoryTailOverlayItem;
import com.valdemar.tellatale.model.Tale;
import com.valdemar.tellatale.util.LatLonPoint;
import com.valdemar.tellatale.util.StringUtils;

public class TalesActivity extends TaleBaseMapActivity {

	protected static final int LOCATION_ACTION_REQUEST_CODE = 3;
	private static final float RADIUS = 15000;
	private Location current_location = null;

	// Acquire a reference to the system Location Manager
	LocationManager locationManager = null;

	MapView mapView;
	Tale[] tales = null;
	TextView numTails;
	LatLonPoint currentLocation = null;
	List<Overlay> mapOverlays = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tale_map_layout);

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(false);
		numTails = (TextView) findViewById(R.id.num_tales);
		numTails.setText(R.string.lbl_waiting_for_gps);

		mapView.getController().setZoom(15);

		mapOverlays = mapView.getOverlays();

		mapOverlays.add(new MyOverlay());

		checkEnableGPS();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add("Add").setOnMenuItemClickListener(addMenuItemClickListener).setIcon(R.drawable.icon_new).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		// menu.add("Search")
		// .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
		// MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		//
		// menu.add("Refresh")
		// .setIcon(isLight ? R.drawable.ic_refresh_inverse :
		// R.drawable.ic_refresh)
		// .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
		// MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	OnMenuItemClickListener addMenuItemClickListener = new OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem item) {

			if (current_location == null) {
				Toast.makeText(getBaseContext(), R.string.lbl_cannot_start_tail_no_location, Toast.LENGTH_SHORT).show();
				return true;
			}
			Intent intent = new Intent(getBaseContext(), StartTaleActivity.class);
			intent.putExtra(StartTaleActivity.LOCATION_KEY, current_location);
			startActivity(intent);
			return true;
		}
	};

	private void setCurrentLocation(Location location) {

		// I'm removing the location listener...
		locationManager.removeUpdates(locationListener);

		currentLocation = new LatLonPoint(location.getLatitude(), location.getLongitude());
		mapView.getController().setCenter(currentLocation);

		mapOverlays.add(new CircleOverlay(this, location.getLatitude(), location.getLongitude(), RADIUS));

		getTales(location);

	}

	private void getCurrentLocation() {

		boolean gps_enabled = false;
		boolean network_enabled = false;

		try {
			gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		}
		catch (Exception ex) {}
		try {
			network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}
		catch (Exception ex) {}

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled) {
			Toast.makeText(this, "Sorry, location is not determined. To fix this please enable location providers", Toast.LENGTH_SHORT).show();
		}

		setSupportProgressBarIndeterminateVisibility(true);

		if (gps_enabled) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		}
		if (network_enabled) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		}

	}

	private void checkEnableGPS() {
		String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (provider.contains("gps")) {
			getCurrentLocation();
			return; // the GPS is already in the requested state
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.lbl_enable_gps).setCancelable(false).setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {

				Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(gpsOptionsIntent, LOCATION_ACTION_REQUEST_CODE);

				dialog.dismiss();

			}
		}).setNegativeButton(R.string.lbl_no, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

				getCurrentLocation();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();

		return;

	}

	private void getTales(Location location) {

		setSupportProgressBarIndeterminateVisibility(true);

		numTails.setText(R.string.lbl_loading_stories);

		StackMobGeoPoint sf = new StackMobGeoPoint(location.getLongitude(), location.getLatitude());
		StackMobQuery q = new StackMobQuery().fieldIsNearWithinKm(Tale.CURRENTCOORD_FIELD_NAME, sf, (double)RADIUS / 1000); //(lon, lat), distance (miles)
		Tale.query(Tale.class, q, new StackMobQueryCallback<Tale>() {
		
		//Tale.query(Tale.class, new StackMobQuery(), StackMobOptions.depthOf(1), new StackMobQueryCallback<Tale>() {

			@Override
			public void success(List<Tale> tales) {
				List<Overlay> mapOverlays = mapView.getOverlays();

				ArrayList<StoryTailOverlayItem> items = new ArrayList<StoryTailOverlayItem>();

				GeoPoint point;
				String title = "";
				final int talesSize = tales.size();

				int index = 1;
				for (Tale tale : tales) {
					title = tale.getTitle();
					if (TextUtils.isEmpty(title))
						title = "";

					point = new LatLonPoint(tale.getCurrentcoord().getLatitude(), tale.getCurrentcoord().getLongitude());
					StoryTailOverlayItem item = new StoryTailOverlayItem(tale, point, "Story Tail", title, writeOnDrawable(R.drawable.points, Long.toString(tale.getTails().size())));
					items.add(item);
					Log.d("Tale", index + " - " + title + " (" + tale.getCurrentcoord().getLatitude() + "," + tale.getCurrentcoord().getLongitude() + ")");
				}

				StoryTailOverlayItem[] itemsArray = new StoryTailOverlayItem[items.size()];
				items.toArray(itemsArray);

				StoryTailOverlay itemsOverlay = new StoryTailOverlay(getBaseContext(), mapView, itemsArray, onTaleClickEvent);

				mapOverlays.add(itemsOverlay);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						setSupportProgressBarIndeterminateVisibility(false);

						// mProgressBar.setVisibility(View.GONE);
						numTails.setText(getBaseContext().getResources().getQuantityString(R.plurals.lbl_stories_found, talesSize, talesSize));
						mapView.invalidate();
					}
				});
			}

			@Override
			public void failure(StackMobException e) {

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						setSupportProgressBarIndeterminateVisibility(false);
						numTails.setText(R.string.lbl_error_finding_tales);

					}
				});
			}
		});
	}

	OnItemClickEvent onTaleClickEvent = new OnItemClickEvent() {

		@Override
		public void OnClick(String taleId) {
			Intent intent = new Intent(getBaseContext(), TaleListActivity.class);
			intent.putExtra(TaleListActivity.TALE_ID_KEY, taleId);
			startActivity(intent);
		}
	};

	public BitmapDrawable writeOnDrawable(int drawableId, String text) {

		Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

		Typeface tf = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.GRAY);
		paint.setAntiAlias(true);

		Canvas canvas = new Canvas(bm);

		// canvas.drawCircle(bm.getWidth() / 2, bm.getHeight() / 2 - 6,
		// bm.getHeight() / 2 - 15, paint);

		paint.setColor(Color.WHITE);
		paint.setTextSize(30);
		paint.setTypeface(tf);
		paint.setShadowLayer(2f, 1, 1, Color.BLACK);

		int bitmapMidle = bm.getWidth() / 2;

		int textMidle = StringUtils.getStringWidth(paint, text) / 2;

		bitmapMidle = bitmapMidle - textMidle;

		canvas.drawText(text, bitmapMidle, bm.getHeight() / 2, paint);

		return new BitmapDrawable(bm);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			// Called when a new location is found by the network location
			// provider.

			current_location = location;
			setSupportProgressBarIndeterminateVisibility(false);

			setCurrentLocation(location);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {}

		public void onProviderEnabled(String provider) {}

		public void onProviderDisabled(String provider) {}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
			case LOCATION_ACTION_REQUEST_CODE:
				checkEnableGPS();
				break;

			default:
				break;
		}
	};

}
