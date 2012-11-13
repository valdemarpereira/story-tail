package com.valdemar.tellatale.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.valdemar.tellatale.util.LatLonPoint;

public class CircleOverlay extends Overlay {

	Context context;
	GeoPoint geo;
	float mRadius;
	Double mLatitude;

	public CircleOverlay(Context _context, Double latitude, Double longitude,
			float radius) {
		context = _context;
		geo = new LatLonPoint(latitude, longitude);
		mRadius = radius;
		mLatitude = latitude;
	}

	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);

		if (shadow)
			return; // Ignore the shadow layer

		Projection projection = mapView.getProjection();

		Point pt = new Point();

		projection.toPixels(geo, pt);
		float circleRadius = metersToRadius(mRadius, mapView, mLatitude);

		Paint innerCirclePaint;

		innerCirclePaint = new Paint();
		innerCirclePaint.setColor(Color.BLUE);
		innerCirclePaint.setAlpha(25);
		innerCirclePaint.setAntiAlias(true);

		innerCirclePaint.setStyle(Paint.Style.FILL);

		canvas.drawCircle((float) pt.x, (float) pt.y, circleRadius,
				innerCirclePaint);
		
		
		Paint pointPaint = new Paint();
		pointPaint.setColor(Color.BLACK);
		//pointPaint.setAlpha(25);
		pointPaint.setAntiAlias(true);

		pointPaint.setStyle(Paint.Style.FILL);
		canvas.drawCircle((float) pt.x, (float) pt.y, 3, pointPaint);
		// TODO: Draw a point
		// TODO: Draw a circle stroke
	}

	public static int metersToRadius(float meters, MapView map, double latitude) {
		return (int) (map.getProjection().metersToEquatorPixels(meters) * (1 / Math
				.cos(Math.toRadians(latitude))));
	}
}