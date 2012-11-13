package com.valdemar.tellatale.map;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.valdemar.tellatale.model.Tale;

public class StoryTailOverlayItem extends OverlayItem
{
	Drawable marker = null;
	boolean isHeart = false;
	private Tale mTale;

	public StoryTailOverlayItem(Tale tale, GeoPoint pt, String name, String snippet, Drawable marker)
	{
		super(pt, name, snippet);
		mTale = tale;
		this.marker = marker;
	}

	public Drawable getDrawable()
	{
		return (marker);
	}

	@Override
	public Drawable getMarker(int stateBitset)
	{
		setState(marker, stateBitset);

		return (marker);
	}

	public Tale getTale()
	{
		return mTale;
	}

}