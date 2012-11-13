package com.valdemar.tellatale.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.sax.StartElementListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.valdemar.tellatale.R;
import com.valdemar.tellatale.TaleListActivity;
import com.valdemar.tellatale.common.TaleApplicationContext;
import com.valdemar.tellatale.model.Tale;

public class StoryTailOverlay extends ItemizedOverlay<StoryTailOverlayItem>
{
	private MapView mMapView = null;
	private List<StoryTailOverlayItem> items;
	private Context mContext;
	OnItemClickEvent onItemClickEvent;
	
	public interface OnItemClickEvent
	{
		public void OnClick(String taleId);
	}

	public StoryTailOverlay(Context c, MapView map, StoryTailOverlayItem[] itemsPos, OnItemClickEvent onClickEvent)
	{
		super(null);
		onItemClickEvent = onClickEvent;
		mContext = c;
		mMapView = map;
		items = new ArrayList<StoryTailOverlayItem>();

		items.addAll(Arrays.asList(itemsPos));
		Drawable d;

		for (StoryTailOverlayItem storyTailOverlayItem : itemsPos)
		{
			d = storyTailOverlayItem.getDrawable();
			d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			boundCenter(d);
			storyTailOverlayItem.setMarker(d);

		}

		populate();
	}

	@Override
	protected StoryTailOverlayItem createItem(int i)
	{
		return (items.get(i));
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{
		super.draw(canvas, mapView, shadow);

	}

	@Override
	protected boolean onTap(int i)
	{
		 Resources res = mContext.getResources();
		
		 StoryTailOverlayItem item = getItem(i);
		 Tale tale = item.getTale();
		 
		 
		 onItemClickEvent.OnClick( tale.getID());
		 
		//
		// Rect rect = item.getDrawable().getBounds();
		// GeoPoint geo = item.getPoint();
		// PopupPanel panel = new PopupPanel(R.layout.story_detail_ex);
		//
		// Point pt = mMapView.getProjection().toPixels(geo, null);
		//
		// View view = panel.getView();
		//
		// TextView title = (TextView) view.findViewById(R.id.story_title);
		// title.setTypeface(MyAppContext.getTypeface(mContext));
		// title.setText(tale.getTitle());
		//
		// TextView travel = (TextView) view.findViewById(R.id.travel_distance);
		// travel.setText("??");
		//
		// TextView away = (TextView) view.findViewById(R.id.away_distance);
		// away.setText("300");
		//
		// TextView interactions = (TextView)
		// view.findViewById(R.id.interactions);
		// interactions.setText(res.getString(R.string.lbl_num_interactions,
		// tale.getCurrentinteractions()));
		//
		// Button btnShowPath = (Button) view.findViewById(R.id.btn_show_path);
		// btnShowPath.setOnClickListener(onShowPathClickListener);
		//
		// panel.show(pt);

		return (true);
	}

	OnClickListener onShowPathClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Toast.makeText(v.getContext(), "la la la", Toast.LENGTH_SHORT).show();

		}
	};

	@Override
	public int size()
	{
		return (items.size());
	}

	private Drawable getMarker(int resource)
	{
		Drawable marker = mContext.getResources().getDrawable(resource);

		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
		boundCenter(marker);

		return (marker);
	}

	// class PopupPanel
	// {
	// View popup;
	// boolean isVisible = false;
	//
	// PopupPanel(int layout) {
	// popup = LayoutInflater.from(mContext).inflate(layout, null, false);
	// }
	//
	// View getView()
	// {
	// return (popup);
	// }
	//
	// void show(Point p)
	// {
	// PopupWindow window;
	// window = new PopupWindow(mContext);
	// window.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
	// window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
	// window.setTouchable(true);
	// window.setFocusable(true);
	// window.setOutsideTouchable(true);
	//
	// window.setContentView(popup);
	// window.setBackgroundDrawable(new BitmapDrawable());
	//
	// popup.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	// int rootWidth = popup.getMeasuredWidth();
	// int rootHeight = popup.getMeasuredHeight();
	//
	// int[] location = new int[2];
	// mMapView.getLocationOnScreen(location);
	//
	// // window.showAtLocation(mMapView, Gravity.NO_GRAVITY, 0, 0);
	//
	// View pointer = popup.findViewById(R.id.pointer);
	//
	// ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams)
	// pointer.getLayoutParams();
	//
	// param.leftMargin = location[0] + p.x - pointer.getWidth() / 2;
	//
	// if (p.y < rootHeight)
	// {
	// window.showAtLocation(mMapView, Gravity.NO_GRAVITY, 0, p.y + 20 +
	// location[1]);
	// }
	// else
	// {
	// window.showAtLocation(mMapView, Gravity.NO_GRAVITY, 0, p.y + location[1]
	// - rootHeight);
	//
	// }
	// }
	// }
}
