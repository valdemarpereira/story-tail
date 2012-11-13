package com.valdemar.tellatale;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.fasterxml.jackson.core.JsonParseException;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.valdemar.tellatale.adapter.TaleTailsAdapter;
import com.valdemar.tellatale.common.TaleApplicationContext;
import com.valdemar.tellatale.model.Tail;
import com.valdemar.tellatale.util.GenericJsonUtil;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaleListActivity extends SherlockMapActivity
{

	public static final String TALE_ID_KEY = "TALE_ID";

	private String tale_id_temp = "9f5452c12dbf47269da588467da6702c";
	ListView tailList;
	TaleTailsAdapter adapter;
	private TaleApplicationContext appContext;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tale_list_layout);
		appContext = (TaleApplicationContext) getApplicationContext();
		tailList = (ListView) findViewById(R.id.tails);
		tailList.setDivider(null);
		loadTails();

	}

	private void loadTails()
	{
		String taleId = "";
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			taleId = extras.getString(TALE_ID_KEY);
		}

		// Read more:
		// http://getablogger.blogspot.com/2008/01/android-pass-data-to-activity.html#ixzz22c0GEryH

		
		Tail.query(Tail.class, new StackMobQuery().fieldIsEqualTo(Tail.OBJECT_NAME, taleId), new StackMobQueryCallback<Tail>() {

			@Override
			public void failure(StackMobException arg0) {
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						//TODO: Guardar log do erro...
						//Erro sem localizaçºão,,,
						
						Toast.makeText(getBaseContext(), "Error... :(", Toast.LENGTH_SHORT);
					}
				});				
			}

			@Override
			public void success(final List<Tail> tails) {
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{

						tailList.addHeaderView(buildHeaderView(tails));

						tailList.addFooterView(buildFooterView(tails));

						adapter = new TaleTailsAdapter(appContext, tails);
						tailList.setAdapter(adapter);

					}

				});				
			}
		});
		
		
		//TODO: VP: teste REfactor
		/*
		StackMobQuery query = new StackMobQuery(Tail.OBJECT_NAME).fieldIsEqualTo(Tail.FIELD_TALE, taleId);
		

		StackMobCommon.getStackMobInstance().get(query, new StackMobCallback()
		{
			@Override
			public void success(String json)
			{
				try
				{
					final Tail[] tails = GenericJsonUtil.INSTANCE.fromJsonString(json, Tail[].class);

					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{

							tailList.addHeaderView(buildHeaderView(tails));

							tailList.addFooterView(buildFooterView(tails));

							adapter = new TaleTailsAdapter(appContext, tails);
							tailList.setAdapter(adapter);

						}

					});
				}
				catch (JsonParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void failure(StackMobException arg0)
			{
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						Toast.makeText(getBaseContext(), "Error... :(", Toast.LENGTH_SHORT);
					}
				});

			}
		});
*/
	}

	private List<Overlay> mapOverlays;

	private Projection projection;
	MapView mapView;
	MapController myMC = null;

	private View buildHeaderView(List<Tail> tails)
	{
		LayoutInflater infalter = getLayoutInflater();
		ViewGroup header = (ViewGroup) infalter.inflate(R.layout.tale_half_map_layout, tailList, false);

		mapView = (MapView) header.findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(false);
		myMC = mapView.getController();
		// TODO: Dummy
		myMC.setCenter(new GeoPoint(19240000, -99120000));
		myMC.setZoom(15);
		mapOverlays = mapView.getOverlays();
		projection = mapView.getProjection();
		mapOverlays.add(new MyOverlay());

		return header;
	}

	private View buildFooterView(List<Tail> tails)
	{
		return new Button(getBaseContext());
	}

	class MyOverlay extends Overlay
	{

		public MyOverlay()
		{

		}

		public void draw(Canvas canvas, MapView mapv, boolean shadow)
		{
			super.draw(canvas, mapv, shadow);

			Paint mPaint = new Paint();
			mPaint.setDither(true);
			mPaint.setColor(Color.RED);
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStrokeWidth(2);

			GeoPoint gP1 = new GeoPoint(19240000, -99120000);
			GeoPoint gP2 = new GeoPoint(37423157, -122085008);

			Point p1 = new Point();
			Point p2 = new Point();
			Path path = new Path();

			projection.toPixels(gP1, p1);
			projection.toPixels(gP2, p2);

			path.moveTo(p2.x, p2.y);
			path.lineTo(p1.x, p1.y);

			canvas.drawPath(path, mPaint);
		}
	}

	// @Override
	// protected void onListItemClick(ListView l, View v, int position, long id)
	// {
	//
	// // get selected items
	// String selectedValue = (String) getListAdapter().getItem(position);
	// Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
	//
	// }

	@Override
	protected boolean isRouteDisplayed()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
