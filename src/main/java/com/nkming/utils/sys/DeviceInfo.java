/*
 * DeviceInfo.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.sys;

import android.content.Context;
import android.util.DisplayMetrics;

import com.nkming.utils.type.Size;
import com.nkming.utils.type.SizeF;

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
}
