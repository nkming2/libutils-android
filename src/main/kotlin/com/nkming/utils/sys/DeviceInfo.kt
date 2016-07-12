package com.nkming.utils.sys;

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.Display
import android.view.WindowManager
import com.nkming.utils.Log
import com.nkming.utils.type.Size
import com.nkming.utils.type.SizeF

/***
 * Retrieve various device information
 */
object DeviceInfo
{
	/**
	 * Get the device screen size in dp
	 *
	 * @param context
	 * @return
	 */
	@JvmStatic
	fun getScreenDpF(context: Context): SizeF
	{
		val dm = context.resources.displayMetrics
		val w = dm.widthPixels / dm.density
		val h = dm.heightPixels / dm.density
		return SizeF(w, h)
	}

	/**
	 * Get the device screen size in dp, truncated to int
	 *
	 * @param context
	 * @return
	 */
	@JvmStatic
	fun getScreenDp(context: Context): Size
	{
		val sf = getScreenDpF(context)
		return Size(sf.w.toInt(), sf.h.toInt())
	}

	/**
	 * Get the device screen size in sp
	 *
	 * @param context
	 * @return
	 */
	@JvmStatic
	fun getScreenSpF(context: Context): SizeF
	{
		val dm = context.resources.displayMetrics
		val w = dm.widthPixels / dm.scaledDensity
		val h = dm.heightPixels / dm.scaledDensity
		return SizeF(w, h)
	}

	/**
	 * Get the device screen size in sp, truncated to int
	 *
	 * @param context
	 * @return
	 */
	@JvmStatic
	fun getScreenSp(context: Context): Size
	{
		val sf = getScreenSpF(context)
		return Size(sf.w.toInt(), sf.h.toInt())
	}

	/**
	 * Get the device screen size in pixel
	 *
	 * @param context
	 * @return
	 */
	@JvmStatic
	fun getScreenPx(context: Context): Size
	{
		val dm = context.resources.displayMetrics
		return Size(dm.widthPixels, dm.heightPixels)
	}

	/**
	 * Get the device screen size in pixel, including the navigation bar
	 *
	 * @param context
	 * @return
	 */
	@JvmStatic
	fun getFullScreenPx(context: Context): Size
	{
		val wm = context.getSystemService(Context.WINDOW_SERVICE) as
				WindowManager
		val d = wm.defaultDisplay
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
		{
			val p = Point()
			d.getRealSize(p);
			return Size(p.x, p.y)
		}
		else
		{
			try
			{
				val methodGetRawWidth = Display::class.java.getMethod(
						"getRawWidth")
				methodGetRawWidth.isAccessible = true

				val methodGetRawHeight = Display::class.java.getMethod(
						"getRawHeight")
				methodGetRawHeight.isAccessible = true
				return Size(methodGetRawWidth.invoke(d) as Int,
						methodGetRawHeight.invoke(d) as Int)
			}
			catch (e: Exception)
			{
				Log.e("$LOG_TAG.getFullScreenPx", "Error while reflection",
						e)
				val p = Point()
				d.getSize(p)
				return Size(p.x, p.y)
			}
		}
	}

	@JvmStatic
	private val LOG_TAG = DeviceInfo::class.java.canonicalName
}
