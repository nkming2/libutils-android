package com.nkming.utils.content;

import android.content.Context;
import androidx.loader.content.AsyncTaskLoader;

public abstract class AsyncTaskLoaderEx<E> extends AsyncTaskLoader<E>
{
	public AsyncTaskLoaderEx(Context context)
	{
		super(context);
	}

	@Override
	public void deliverResult(E data)
	{
		mData = data;
		super.deliverResult(data);
	}

	@Override
	protected void onStartLoading()
	{
		if (takeContentChanged() || mData == null)
		{
			forceLoad();
		}
		else
		{
			deliverResult(mData);
		}
	}

	@Override
	protected void onStopLoading()
	{
		cancelLoad();
	}

	@Override
	protected void onReset()
	{
		super.onReset();
		stopLoading();
		mData = null;
	}

	protected E getData()
	{
		return mData;
	}

	private E mData;
}
