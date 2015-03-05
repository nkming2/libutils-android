/*
 * BitmapCache.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.graphic;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

public class BitmapCache
{
	public static void init(int cacheSize)
	{
		mInstance = new BitmapCache(cacheSize);
	}

	public static void putBitmap(String key, Bitmap bmp)
	{
		get().putBitmap_(key, bmp);
	}

	public static Bitmap getBitmap(String key)
	{
		return get().getBitmap_(key);
	}

	private BitmapCache(int cacheSize)
	{
		mCache = new LruCache(cacheSize)
		{
			@TargetApi(Build.VERSION_CODES.KITKAT)
			@Override
			protected int sizeOf(Object key, Object value)
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
				{
					return ((Bitmap)value).getAllocationByteCount();
				}
				else
				{
					return ((Bitmap)value).getByteCount();
				}
			}
		};
	}

	private static BitmapCache get()
	{
		if (mInstance == null)
		{
			throw new IllegalStateException("Must call init() first");
		}
		return mInstance;
	}

	private void putBitmap_(String key, Bitmap bmp)
	{
		mCache.put(key, bmp);
	}

	private Bitmap getBitmap_(String key)
	{
		return mCache.get(key);
	}

	private LruCache<String, Bitmap> mCache;

	private static BitmapCache mInstance;
}
