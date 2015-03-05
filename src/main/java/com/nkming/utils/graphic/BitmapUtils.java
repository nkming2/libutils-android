/*
 * BitmapUtils.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.graphic;

import android.graphics.BitmapFactory;

import com.nkming.utils.Log;
import com.nkming.utils.Res;
import com.nkming.utils.type.Size;

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

	private static final String LOG_TAG = Res.LOG_TAG + "."
			+ BitmapUtils.class.getSimpleName();
}
