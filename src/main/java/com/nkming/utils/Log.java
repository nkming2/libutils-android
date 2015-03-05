package com.nkming.utils;

public class Log
{
	public static int wtf(String tag, String msg)
	{
		// Always show.
		return android.util.Log.wtf(tag, msg);
	}

	public static int wtf(String tag, String msg, Throwable tr)
	{
		// Always show.
		return android.util.Log.wtf(tag, msg, tr);
	}

	public static int e(String tag, String msg)
	{
		// Always show.
		return android.util.Log.e(tag, msg);
	}

	public static int e(String tag, String msg, Throwable tr)
	{
		// Always show.
		return android.util.Log.e(tag, msg, tr);
	}

	public static int w(String tag, String msg)
	{
		// Always show.
		return android.util.Log.w(tag, msg);
	}

	public static int w(String tag, String msg, Throwable tr)
	{
		// Always show.
		return android.util.Log.w(tag, msg, tr);
	}

	public static int i(String tag, String msg)
	{
		if (isShowInfo)
		{
			return android.util.Log.i(tag, msg);
		}
		else
		{
			return -1;
		}
	}

	public static int i(String tag, String msg, Throwable tr)
	{
		if (isShowInfo)
		{
			return android.util.Log.i(tag, msg, tr);
		}
		else
		{
			return -1;
		}
	}

	public static int d(String tag, String msg)
	{
		if (isShowDebug)
		{
			return android.util.Log.d(tag, msg);
		}
		else
		{
			return -1;
		}
	}

	public static int d(String tag, String msg, Throwable tr)
	{
		if (isShowDebug)
		{
			return android.util.Log.d(tag, msg, tr);
		}
		else
		{
			return -1;
		}
	}

	public static int v(String tag, String msg)
	{
		if (isShowVerbose)
		{
			return android.util.Log.v(tag, msg);
		}
		else
		{
			return -1;
		}
	}

	public static int v(String tag, String msg, Throwable tr)
	{
		if (isShowVerbose)
		{
			return android.util.Log.v(tag, msg, tr);
		}
		else
		{
			return -1;
		}
	}

	public static boolean isShowInfo = true;
	public static boolean isShowDebug = false;
	public static boolean isShowVerbose = false;
}
