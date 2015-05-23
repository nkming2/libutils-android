/*
 * PersistentService.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;

import com.nkming.utils.Log;

public abstract class PersistentService extends Service
{
	/**
	 * Start the service
	 *
	 * @param context
	 */
	public static void start(Context context)
	{
		Intent intent = new Intent(context, PersistentService.class);
		context.startService(intent);
	}

	/**
	 * Stop the service
	 *
	 * @param context
	 */
	public static void stop(Context context)
	{
		Intent intent = new Intent(context, PersistentService.class);
		intent.setAction(ACTION_STOP);
		context.startService(intent);
	}

	/**
	 * Show the persistent view managed by this service. The view will be shown
	 * automatically during the start of this service so you only need to call
	 * this after hideView()
	 *
	 * @param context
	 * @see PersistentService#hideView(android.content.Context)
	 */
	public static void showView(Context context)
	{
		Intent intent = new Intent(context, PersistentService.class);
		intent.setAction(ACTION_SHOW);
		context.startService(intent);
	}

	/**
	 * Temporarily hide the persistent view
	 *
	 * @param context
	 * @see PersistentService#showView(android.content.Context)
	 */
	public static void hideView(Context context)
	{
		Intent intent = new Intent(context, PersistentService.class);
		intent.setAction(ACTION_HIDE);
		context.startService(intent);
	}

	/**
	 * To hide the persistent view when running a fullscreen app
	 *
	 * @param flag
	 */
	public static void setAutohideView(Context context, boolean flag)
	{
		Intent intent = new Intent(context, PersistentService.class);
		intent.setAction(ACTION_AUTOHIDE);
		intent.putExtra(EXTRA_AUTOHIDE, flag);
		context.startService(intent);
	}

	/**
	 * Return if the service is running or not
	 *
	 * @return
	 */
	public static boolean isRunning()
	{
		return mIsRunning;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		Log.d(LOG_TAG, "onCreate");
		mIsRunning = true;
		super.onCreate();
		initView();
		startForeground(1, getForegroundNotification());
	}

	@Override
	public void onDestroy()
	{
		Log.d(LOG_TAG, "onDestroy");
		mIsRunning = false;
		super.onDestroy();
		uninitForeground();
		uninitView();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (intent != null && intent.getAction() != null)
		{
			switch (intent.getAction())
			{
			case ACTION_STOP:
				stop();
				break;

			case ACTION_SHOW:
				show();
				break;

			case ACTION_HIDE:
				hide();
				break;

			case ACTION_AUTOHIDE:
				mView.setAutohide(intent.getBooleanExtra(EXTRA_AUTOHIDE, false));
				break;
			}
		}
		return START_STICKY;
	}

	/**
	 * Return the layout resource for the persistent view
	 *
	 * @return
	 */
	protected abstract int getLayoutId();
	protected abstract Notification getForegroundNotification();

	/**
	 * Return the persistent view managed by this service, could be null
	 *
	 * @return
	 */
	protected PersistentView getView()
	{
		return mView;
	}

	protected void onInitView()
	{}

	protected void onViewClick()
	{
		Log.d(LOG_TAG, "onViewClick");
	}

	protected void onViewLongClick()
	{
		Log.d(LOG_TAG, "onViewLongClick");
	}

	private static final String LOG_TAG =
			PersistentService.class.getCanonicalName();

	private static final String ACTION_STOP = "stop";
	private static final String ACTION_SHOW = "show";
	private static final String ACTION_HIDE = "hide";
	private static final String ACTION_AUTOHIDE = "autohide";
	private static final String EXTRA_AUTOHIDE = "autohide";

	private void initView()
	{
		if (mView != null)
		{
			uninitView();
		}

		PersistentView.Config conf = new PersistentView.Config();
		conf.handler = new Handler();
		conf.context = this;
		conf.resId = getLayoutId();
		conf.alpha = 0.5f;
		conf.hiddenW = 0.35f;
		mView = new PersistentView(conf);
		mView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onViewClick();
			}
		});
		mView.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				onViewLongClick();
				return true;
			}
		});

		onInitView();
	}

	private void uninitView()
	{
		if (mView != null)
		{
			mView.destroy();
			mView = null;
		}
	}

	private void uninitForeground()
	{
		stopForeground(true);
	}

	private void stop()
	{
		mView.hide(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				stopSelf();
			}
		});
	}

	private void show()
	{
		mView.show(null);
	}

	private void hide()
	{
		mView.hide(null);
	}

	private PersistentView mView;

	private static boolean mIsRunning = false;
}
