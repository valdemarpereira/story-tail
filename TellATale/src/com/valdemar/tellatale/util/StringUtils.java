package com.valdemar.tellatale.util;

import android.graphics.Paint;
import android.graphics.Rect;

public class StringUtils
{
	public static int getStringWidth(Paint paint, String str)
	{
		Rect bounds = new Rect();
		paint.getTextBounds(str, 0, str.length(), bounds);
		int height = bounds.height();
		return bounds.width();
	}
}
