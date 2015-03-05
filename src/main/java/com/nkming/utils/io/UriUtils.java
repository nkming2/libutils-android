package com.nkming.utils.io;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.nkming.utils.Log;
import com.nkming.utils.Res;

public class UriUtils
{
	/**
	 * Convert a Uri of a file to its path, the Uri can be a file Uri or a
	 * content Uri.
	 *
	 * @param context
	 * @param uri The Uri willing to be converted
	 * @return The file path, null if failed.
	 */
	public static String contentUriToPath(Context context, Uri uri)
	{
		Log.v(LOG_TAG + ".contentUriToPath",
				String.format("(..., %s)", uri.toString()));

		String scheme = uri.getScheme();
		if (scheme == null)
		{
			Log.e(LOG_TAG + ".contentUriToPath", "Missing scheme");
			return null;
		}
		if (scheme.equals(ContentResolver.SCHEME_FILE))
		{
			return uri.getPath();
		}
		else if (scheme.equals(ContentResolver.SCHEME_CONTENT))
		{
			String[] projection = {"_id", MediaStore.MediaColumns.DATA};
			Cursor cursor = context.getContentResolver().query(uri, projection,
					null, null, null);
			try
			{
				if (cursor == null)
				{
					Log.e(LOG_TAG + ".contentUriToPath", "cursor == null");
					return null;
				}

				cursor.moveToFirst();
				int columnId = cursor.getColumnIndex(
						MediaStore.MediaColumns.DATA);
				if (columnId == -1)
				{
					Log.e(LOG_TAG + ".contentUriToPath", "columnId == null");
					return null;
				}

				return cursor.getString(columnId);
			}
			finally
			{
				if (cursor != null)
				{
					cursor.close();
				}
			}
		}
		else
		{
			Log.e(LOG_TAG + ".contentUriToPath", String.format(
					"Unknown uri. (%s)", uri.toString()));
			return null;
		}
	}

	private static final String LOG_TAG = Res.LOG_TAG + "."
			+ UriUtils.class.getSimpleName();
}
