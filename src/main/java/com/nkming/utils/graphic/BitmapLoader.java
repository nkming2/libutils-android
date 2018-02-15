/*
 * BitmapLoader.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.graphic;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.nkming.utils.Log;
import com.nkming.utils.Res;
import com.nkming.utils.io.UriUtils;
import com.nkming.utils.type.Size;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * A universal Bitmap loader that loads Bitmap from local file, media content
 * Uri, android resource, or remote location through HTTP
 */
public class BitmapLoader
{
	public BitmapLoader(Context context)
	{
		mContext = context;
	}

	/**
	 * Load a Bitmap from a location specified by @a uri
	 *
	 * @param uri
	 * @return The bitmap if successful, null otherwise
	 */
	public Bitmap loadUri(Uri uri)
	{
		try
		{
			String scheme = uri.getScheme();
			if (scheme == null)
			{
				Log.e(LOG_TAG + ".loadUri", "Missing scheme");
				return null;
			}
			else if (scheme.equals(ContentResolver.SCHEME_FILE)
					|| scheme.equals(ContentResolver.SCHEME_CONTENT))
			{
				return loadFileOrMedia(uri);
			}
			else if (scheme.equals(ContentResolver.SCHEME_ANDROID_RESOURCE))
			{
				return loadResource(uri);
			}
			else if (scheme.equals("http") || scheme.equals("https"))
			{
				return loadHttp(uri);
			}
			else
			{
				Log.e(LOG_TAG + ".loadUri", String.format(
						"Unable to load uri (%s)", uri.toString()));
				return null;
			}
		}
		catch (SecurityException e)
		{
			Log.e(LOG_TAG + ".loadUri",
					"Missing READ_EXTERNAL_STORAGE permission?", e);
			return null;
		}
	}

	/**
	 * Specify a size to load at. If not set, the Bitmap loaded will keep its
	 * original size
	 *
	 * @param size
	 * @return
	 * @see BitmapLoader#setSizeCalc(SizeCalc)
	 */
	public BitmapLoader setTargetSize(Size size)
	{
		mTargetSize = (size == null) ? new Size(0, 0) : size;
		return this;
	}

	/**
	 * Specify a resize strategy if a target size is set
	 *
	 * @param sc
	 * @return
	 * @see BitmapLoader#setTargetSize(Size)
	 */
	public BitmapLoader setSizeCalc(SizeCalc sc)
	{
		mSizeCalc = (sc == null) ? new NullSizeCalc() : sc;
		return this;
	}

	private Bitmap loadFileOrMedia(Uri uri)
	{
		String path = UriUtils.contentUriToPath(mContext, uri);
		if (!new File(path).exists())
		{
			Log.e(LOG_TAG + ".loadFile", "File not exists");
			return null;
		}

		if (mTargetSize.w() > 0 && mTargetSize.h() > 0)
		{
			// Load at specific size
			Size origSize = BitmapUtils.getSize(path);
			if (origSize == null)
			{
				Log.e(LOG_TAG + ".loadFile", "Failed while getSize");
				return null;
			}
			Size bestSize = mSizeCalc.calc(origSize, mTargetSize);

			BitmapFactory.Options ops = new BitmapFactory.Options();
			int sampleSize = getSampleSize(origSize, bestSize);
			if (sampleSize >= 2)
			{
				ops.inSampleSize = sampleSize;
			}
			Bitmap product = BitmapFactory.decodeFile(path, ops);
			if (product == null)
			{
				Log.e(LOG_TAG + ".loadFile", "Failed while decodeFile");
				return null;
			}
			else
			{
				return scaleBitmap(product, bestSize);
			}
		}
		else
		{
			return BitmapFactory.decodeFile(path);
		}
	}

	private Bitmap loadResource(Uri uri)
	{
		try
		{
			String packageName = uri.getAuthority();
			Resources res = mContext.getPackageManager()
					.getResourcesForApplication(packageName);
			List<String> pathSegs =  uri.getPathSegments();
			if (pathSegs.size() == 1)
			{
				// Resource uri with id
				return loadResourceById(res, packageName, Integer.parseInt(
						pathSegs.get(0)));
			}
			else
			{
				// Resource uri with name
				int resId = res.getIdentifier(pathSegs.get(1), pathSegs.get(0),
						packageName);
				if (resId == 0)
				{
					Log.e(LOG_TAG + ".loadResource", "Failed while getIdentifier");
					return null;
				}
				else
				{
					return loadResourceById(res, packageName, resId);
				}
			}
		}
		catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private Bitmap loadResourceById(Resources res, String packageName, int resId)
	{
		int density = res.getDisplayMetrics().densityDpi;
		Drawable d = res.getDrawableForDensity(resId, density);

		if (mTargetSize.w() > 0 && mTargetSize.h() > 0)
		{
			Bitmap product = DrawableUtils.toBitmap(d, mTargetSize.w(),
					mTargetSize.h());
			Size origSize = new Size(product.getWidth(), product.getHeight());
			return scaleBitmap(product, mSizeCalc.calc(origSize, mTargetSize));
		}
		else
		{
			return DrawableUtils.toBitmap(d, 100, 100);
		}
	}

	private Bitmap loadHttp(Uri uri)
	{
		Bitmap bmp = null;
		InputStream in = null;
		try
		{
			URL url = new URL(uri.toString());
			in = url.openStream();

			if (mTargetSize.w() > 0 && mTargetSize.h() > 0)
			{
				Size origSize = BitmapUtils.getSize(in);
				in.close();
				if (origSize == null)
				{
					Log.e(LOG_TAG + ".loadHttp", "Failed while getSize");
					return null;
				}
				Size bestSize = mSizeCalc.calc(new Size(origSize.w(), origSize.h()),
						mTargetSize);

				BitmapFactory.Options ops = new BitmapFactory.Options();
				int sampleSize = getSampleSize(origSize, bestSize);
				if (sampleSize >= 2)
				{
					ops.inSampleSize = sampleSize;
				}
				in = url.openStream();
				Bitmap product = BitmapFactory.decodeStream(in);
				if (product == null)
				{
					Log.e(LOG_TAG + ".loadHttp", "Failed while decodeFile");
					return null;
				}
				else
				{
					return scaleBitmap(product, bestSize);
				}
			}
			else
			{
				return BitmapFactory.decodeStream(in);
			}
		}
		catch (Exception e)
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (Exception e2)
				{}
			}
			Log.w(LOG_TAG, "Exception", e);
		}
		return bmp;
	}

	private Bitmap scaleBitmap(Bitmap orig, Size targetSize)
	{
		Size origSize = new Size(orig.getWidth(), orig.getHeight());
		if (!origSize.equals(targetSize))
		{
			Bitmap resized = Bitmap.createScaledBitmap(orig, targetSize.w(),
					targetSize.h(), true);
			orig.recycle();
			return resized;
		}
		else
		{
			return orig;
		}
	}

	private int getSampleSize(Size origSize, Size targetSize)
	{
		int ratio = Math.min(origSize.w() / targetSize.w(),
				origSize.h() / targetSize.h());
		int product = 1;
		for (int test = 2; true; test *= 2)
		{
			if (test > ratio)
			{
				return product;
			}
			++product;
		}
	}

	private static final String LOG_TAG = Res.LOG_TAG + "."
			+ BitmapLoader.class.getSimpleName();

	private Context mContext;
	private Size mTargetSize = new Size(0, 0);
	private SizeCalc mSizeCalc = new NullSizeCalc();
}
