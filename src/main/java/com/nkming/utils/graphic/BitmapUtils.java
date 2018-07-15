package com.nkming.utils.graphic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nkming.utils.Log;
import com.nkming.utils.Res;
import com.nkming.utils.type.Size;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
	 * Return the size of a bitmap without loading the whole file
	 *
	 * @param stream
	 * @return
	 */
	public static Size getSize(InputStream stream)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeStream(stream, null, options);
		if (options.outWidth == -1 || options.outHeight == -1)
		{
			Log.e(LOG_TAG + ".getSize", "Failed while decodeStream");
			return null;
		}
		else
		{
			return new Size(options.outWidth, options.outHeight);
		}
	}

	/**
	 * Save a bitmap to a specific path
	 *
	 * @param bmp
	 * @param path
	 * @param format
	 * @param quality
	 * @return
	 */
	public static boolean saveBitmap(Bitmap bmp, String path,
			Bitmap.CompressFormat format, int quality)
	{
		File file = new File(path);
		OutputStream os = null;
		try
		{
			os = new FileOutputStream(file);
			os = new BufferedOutputStream(os);
			bmp.compress(format, quality, os);
			return true;
		}
		catch (IOException e)
		{
			Log.e(LOG_TAG + ".saveBitmap", "IOException", e);
			return false;
		}
		finally
		{
			if (os != null)
			{
				try
				{
					os.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private static final String LOG_TAG = Res.LOG_TAG + "."
			+ BitmapUtils.class.getSimpleName();
}
