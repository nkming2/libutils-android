package com.nkming.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.Map;

public abstract class DebugActivity extends AppCompatActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Log.isShowDebug = isDefaultShowDebugLog();
		Log.isShowVerbose = isDefaultShowVerboseLog();

		setContentView(R.layout.util_debug);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.util_debug, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.menu_app_info)
		{
			onAppInfoClick();
			return true;
		}
		else
		{
			return super.onOptionsItemSelected(item);
		}
	}

	protected View onCreateCustomView(ViewGroup container)
	{
		return null;
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (mDialog != null && mDialog.isShowing())
		{
			mDialog.dismiss();
			mDialog = null;
		}
	}

	protected void onAppInfoClick()
	{
		Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		i.setData(Uri.parse("package:" + getApplicationInfo().packageName));
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}

	protected void onDebugLogChange(boolean isChecked)
	{
		Log.isShowDebug = isChecked;
	}

	protected void onLaunchAppClick()
	{
		final Map<String, Class<?>> activities = getActivityClasses();
		final String[] keys = activities.keySet().toArray(new String[0]);
		if (activities.size() == 1)
		{
			launchActivity(activities.get(keys[0]));
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select activity to launch")
				.setItems(keys, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog,
										int which)
					{
						launchActivity(activities.get(keys[which]));
					}
				});
		mDialog = builder.create();
		mDialog.show();
	}

	protected void onVerboseLogChange(boolean isChecked)
	{
		Log.isShowVerbose = isChecked;
	}

	protected abstract Map<String, Class<?>> getActivityClasses();

	protected boolean isDefaultShowDebugLog()
	{
		return false;
	}

	protected boolean isDefaultShowVerboseLog()
	{
		return false;
	}

	private void init()
	{
		initCustomView();
		initLog();
		initLaunchApp();
	}

	private void initCustomView()
	{
		ViewGroup container = (ViewGroup)findViewById(R.id.custom_view_container);
		View v = onCreateCustomView(container);
		if (v == null)
		{
			return;
		}
		container.addView(v);
	}

	private void initLog()
	{
		CheckBox debug = (CheckBox)findViewById(R.id.debug_log);
		debug.setChecked(Log.isShowDebug);
		debug.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked)
			{
				onDebugLogChange(isChecked);
			}
		});

		CheckBox verbose = (CheckBox)findViewById(R.id.verbose_log);
		verbose.setChecked(Log.isShowVerbose);
		verbose.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked)
			{
				onVerboseLogChange(isChecked);
			}
		});
	}

	private void initLaunchApp()
	{
		Button btn = (Button)findViewById(R.id.launch_app);
		btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onLaunchAppClick();
			}
		});
	}

	private void launchActivity(Class<?> activityClass)
	{
		Intent i = new Intent(this, activityClass);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);

		finish();
	}

	private Dialog mDialog;
}
