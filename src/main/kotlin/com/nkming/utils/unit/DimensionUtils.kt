package com.nkming.utils.unit;

import android.content.Context
import android.util.TypedValue

class DimensionUtils
{
	companion object
	{
		@JvmStatic
		fun dpToPx(context: Context, dp: Float): Float
		{
			val dm = context.resources.displayMetrics
			return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm)
		}

		@JvmStatic
		fun spToPx(context: Context, sp: Float): Float
		{
			val dm = context.resources.displayMetrics
			return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, dm)
		}

		@JvmStatic
		fun pxToDp(context: Context, px: Float): Float
		{
			val dm = context.resources.displayMetrics
			return px / dm.density
		}

		@JvmStatic
		fun pxToSp(context: Context, px: Float): Float
		{
			val dm = context.resources.displayMetrics
			return px / dm.scaledDensity
		}
	}
}
