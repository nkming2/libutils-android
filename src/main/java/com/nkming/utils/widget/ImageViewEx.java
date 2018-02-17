package com.nkming.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nkming.utils.Log;
import com.nkming.utils.R;
import com.nkming.utils.Res;
import com.nkming.utils.graphic.BitmapLoader;
import com.nkming.utils.graphic.FillSizeCalc;
import com.nkming.utils.graphic.FitSizeCalc;
import com.nkming.utils.graphic.NullSizeCalc;
import com.nkming.utils.graphic.SizeCalc;
import com.nkming.utils.graphic.StretchSizeCalc;
import com.nkming.utils.io.UriUtils;
import com.nkming.utils.type.Size;

/**
 * A custom ImageView with various extra features
 *
 * async: load the bitmap in another thread<br>
 * loadOptimalSize: load the bitmap at the size of the view
 */
public class ImageViewEx extends AppCompatImageView
{
	public ImageViewEx(Context context)
	{
		super(context);
	}

	public ImageViewEx(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initAttribute(attrs);
	}

	public ImageViewEx(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initAttribute(attrs);
	}

	@Override
	public void setImageURI(Uri uri)
	{
		Log.v(LOG_TAG, "setImageURI " + uri);
		mUri = uri;
		mHasLoad = false;
		mIsLoading = false;
		if (mIsLoadOptimalSize)
		{
			setVisibility(INVISIBLE);
			if (getWidth() != 0 && getHeight() != 0)
			{
				// Optimal size known, start loading, delay otherwise
				startLoad(uri);
			}
		}
		else if (mIsAsync)
		{
			startLoad(uri);
		}
		else
		{
			nonOverridedOp();
			super.setImageURI(uri);
		}
	}

	@Override
	public void setImageResource(int resId)
	{
		if (mIsLoadOptimalSize || mIsAsync)
		{
			Uri uri = UriUtils.getResourceUri(getContext().getPackageName(),
					resId);
			setImageURI(uri);
		}
		else
		{
			nonOverridedOp();
			super.setImageResource(resId);
		}
	}

	@Override
	public void setImageBitmap(Bitmap bm)
	{
		nonOverridedOp();
		super.setImageBitmap(bm);
	}

	@Override
	public void setImageDrawable(Drawable drawable)
	{
		nonOverridedOp();
		super.setImageDrawable(drawable);
	}

	public void setAsync(boolean flag)
	{
		mIsAsync = flag;
	}

	public boolean isAsync()
	{
		return mIsAsync;
	}

	public void setLoadOptimalSize(boolean flag)
	{
		mIsLoadOptimalSize = flag;
	}

	public boolean isLoadOptimalSize()
	{
		return mIsLoadOptimalSize;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom)
	{
		Log.v(LOG_TAG, "onLayout " + mUri);
		super.onLayout(changed, left, top, right, bottom);
	}

	protected void onAttachedToWindow()
	{
		Log.v(LOG_TAG, "onAttachedToWindow " + mUri);
		super.onAttachedToWindow();
		if (mUri != null && !mHasLoad && !mIsLoading)
		{
			startLoad(mUri);
		}
	}

	@Override
	protected void onDetachedFromWindow()
	{
		Log.v(LOG_TAG, "onDetachedFromWindow " + mUri);
		stopLoad();
		super.onDetachedFromWindow();
	}

	private class BitmapLoaderTask extends AsyncTask<Uri, Void, Bitmap>
	{
		@Override
		protected Bitmap doInBackground(Uri... params)
		{
			return loadBitmap(params[0]);
		}

		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			showBitmap(bitmap);
			mTask = null;
		}
	}

	private static final String LOG_TAG = Res.LOG_TAG + "."
			+ ImageViewEx.class.getSimpleName();

	private void initAttribute(AttributeSet attrs)
	{
		TypedArray ta = getContext().obtainStyledAttributes(attrs,
				R.styleable.ImageViewEx);
		mIsAsync = ta.getBoolean(R.styleable.ImageViewEx_async, false);
		mIsLoadOptimalSize = ta.getBoolean(
				R.styleable.ImageViewEx_loadOptimalSize, false);

		ta.recycle();
	}

	private void startLoad(Uri uri)
	{
		if (mIsAsync)
		{
			stopLoad();
			mTask = new BitmapLoaderTask();
			mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri);
			mIsLoading = true;
		}
		else
		{
			showBitmap(loadBitmap(uri));
		}
	}

	private void stopLoad()
	{
		Log.v(LOG_TAG, "stopLoad " + mUri);
		if (mTask != null)
		{
			mTask.cancel(true);
			mIsLoading = false;
		}
	}

	private Bitmap loadBitmap(Uri uri)
	{
		BitmapLoader loader = new BitmapLoader(getContext());
		if (mIsLoadOptimalSize)
		{
			loader.setTargetSize(new Size(getWidth(), getHeight()));
			loader.setSizeCalc(getSizeCalc());
		}
		return loader.loadUri(uri);
	}

	private void showBitmap(Bitmap bmp)
	{
		Log.v(LOG_TAG, "showBitmap " + mUri);
		mHasLoad = true;
		mIsLoading = false;
		setAlpha(0.0f);
		setImageBitmap(bmp);
		animate().alpha(1.0f)
				.setInterpolator(new AccelerateDecelerateInterpolator())
				.setDuration(500);
	}

	private void nonOverridedOp()
	{
		stopLoad();
		mHasLoad = true;
		mUri = null;
		setVisibility(VISIBLE);
	}

	private SizeCalc getSizeCalc()
	{
		SizeCalc sc;
		if (!mIsLoadOptimalSize)
		{
			sc = new NullSizeCalc();
		}
		else
		{
			switch (getScaleType())
			{
			case CENTER:
			default:
				sc = new NullSizeCalc();
				break;

			case CENTER_CROP:
				sc = new FillSizeCalc();
				break;

			case CENTER_INSIDE:
				sc = new FitSizeCalc();
				sc.setAllowUpscale(false);
				break;

			case FIT_CENTER:
			case FIT_END:
			case FIT_START:
				sc = new FitSizeCalc();
				break;

			case FIT_XY:
				sc = new StretchSizeCalc();
				break;
			}
		}
		return sc;
	}

	private boolean mIsAsync = false;
	private boolean mIsLoadOptimalSize = false;

	private Uri mUri = null;
	private boolean mHasLoad = false;
	private boolean mIsLoading = false;
	private BitmapLoaderTask mTask = null;
}
