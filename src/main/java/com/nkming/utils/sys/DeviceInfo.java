/*
 * DeviceInfo.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.sys;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.nkming.utils.Log;
import com.nkming.utils.type.Size;
import com.nkming.utils.type.SizeF;

import java.lang.reflect.Method;

/***
 * Retrieve various device information
 */
public class DeviceInfo
{
	/**
	 * Get the device screen size in dp
	 *
	 * @param context
	 * @return
	 */
	public static SizeF GetScreenDpF(Context context)
	{
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		float w = dm.widthPixels / dm.density;
		float h = dm.heightPixels / dm.density;
		return new SizeF(w, h);
	}

	/**
	 * Get the device screen size in dp, truncated to int
	 *
	 * @param context
	 * @return
	 */
	public static Size GetScreenDp(Context context)
	{
		SizeF sf = GetScreenDpF(context);
		return new Size((int)sf.getWidth(), (int)sf.getHeight());
	}

	/**
	 * Get the device screen size in pixel
	 *
	 * @param context
	 * @return
	 */
	public static Size GetScreenPx(Context context)
	{
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return new Size(dm.widthPixels, dm.heightPixels);
	}

	/**
	 * Get the device screen size in pixel, including the navigation bar
	 *
	 * @param context
	 * @return
	 */
	public static Size GetFullScreenPx(Context context)
	{
		WindowManager wm = (WindowManager)context.getSystemService(
				Context.WINDOW_SERVICE);
		Display d = wm.getDefaultDisplay();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
		{
			Point p = new Point();
			d.getRealSize(p);
			return new Size(p.x, p.y);
		}
		else
		{
			try
			{
				Method methodGetRawWidth = Display.class.getMethod("getRawWidth");
				methodGetRawWidth.setAccessible(true);

				Method methodGetRawHeight = Display.class.getMethod(
						"getRawHeight");
				methodGetRawHeight.setAccessible(true);
				return new Size((Integer)methodGetRawWidth.invoke(d),
						(Integer)methodGetRawHeight.invoke(d));
			}
			catch (Exception e)
			{
				Log.e(LOG_TAG + ".GetFullScreenPx", "Error while reflection", e);
				Point p = new Point();
				d.getSize(p);
				return new Size(p.x, p.y);
			}
		}
	}

	private static final String LOG_TAG = DeviceInfo.class.getCanonicalName();
}
