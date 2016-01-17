/*
 * DimensionUtils.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.unit;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DimensionUtils
{
	public static float dpToPx(Context context, float dp)
	{
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
	}

	public static float spToPx(Context context, float sp)
	{
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, dm);
	}

	public static float pxToDp(Context context, float px)
	{
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return px * (DisplayMetrics.DENSITY_DEFAULT / (float)dm.densityDpi);
	}
}
