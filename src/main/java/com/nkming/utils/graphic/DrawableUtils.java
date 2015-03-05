/*
 * DrawableUtils.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.graphic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class DrawableUtils
{
	/**
	 * Convert a Drawable to Bitmap. For BitmapDrawable, a new clone is created
	 * instead of reusing its internal one
	 *
	 * @param src
	 * @param defaultW The width to use if the Drawable has no fixed size
	 * @param defaultH The height to use if the Drawable has no fixed size
	 * @return
	 */
	public static Bitmap toBitmap(Drawable src, int defaultW, int defaultH)
	{
		int w = src.getIntrinsicWidth();
		if (w == -1)
		{
			w = defaultW;
		}
		int h = src.getIntrinsicHeight();
		if (h == -1)
		{
			h = defaultH;
		}

		Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmp);
		src.setBounds(0, 0, c.getWidth(), c.getHeight());
		src.draw(c);
		return bmp;
	}

	/**
	 * Same as toBitmap(src, 100, 100)
	 *
	 * @param src
	 * @return
	 * @see DrawableUtils.toBitmap(Drawable, int, int)
	 */
	public static Bitmap toBitmap(Drawable src)
	{
		return toBitmap(src, 100, 100);
	}
}
