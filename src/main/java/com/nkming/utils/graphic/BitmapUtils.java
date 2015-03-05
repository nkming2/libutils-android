/*
 * BitmapUtils.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.graphic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nkming.utils.Log;
import com.nkming.utils.Res;
import com.nkming.utils.type.Size;

import java.io.File;

public class BitmapUtils
{
	/**
	 * Return the size of a bitmap without loading the whole file
	 *
	 * @param path
	 * @return
	 */
    public static Size getSize(String path)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        if (options.outWidth == -1 || options.outHeight == -1)
        {
	        Log.e(LOG_TAG + ".getSize", "Failed while decodeFile");
            return null;
        }
        else
        {
            return new Size(options.outWidth, options.outHeight);
        }
    }

	/**
	 * Load a bitmap file at a specific size
	 *
	 * @param path
	 * @param size The target size
	 * @param calc The resizing strategy used
	 * @return The loaded bitmap if successful, null otherwise
	 */
	public static Bitmap loadImageAtSize(String path, Size size, SizeCalc calc)
	{
		if (!new File(path).exists())
		{
			Log.e(LOG_TAG + ".loadImageAtSize", "File not exists");
			return null;
		}

		Size origSize = getSize(path);
		Size bestSize = calc.calc(new Size(origSize.w(), origSize.h()), size);
		int ratio = Math.min(size.w() / bestSize.w(), size.h() / bestSize.h());

		BitmapFactory.Options ops = new BitmapFactory.Options();
		if (ratio >= 2)
		{
			ops.inSampleSize = ratio;
		}
		Bitmap product = BitmapFactory.decodeFile(path, ops);
		if (product == null)
		{
			Log.e(LOG_TAG + ".loadImageAtSize", "Failed while decodeFile");
			return null;
		}

		Size loadedSize = new Size(ops.outWidth, ops.outHeight);
		if (!loadedSize.equals(bestSize))
		{
			Bitmap resized = Bitmap.createScaledBitmap(product, bestSize.w(),
					bestSize.h(), true);
			product.recycle();
			return resized;
		}
		else
		{
			return product;
		}
	}

	private static final String LOG_TAG = Res.LOG_TAG + "."
			+ BitmapUtils.class.getSimpleName();
}
