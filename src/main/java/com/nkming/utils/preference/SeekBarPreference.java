package com.nkming.utils.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.nkming.utils.R;

public class SeekBarPreference extends DialogPreference
{
	public interface PreviewListener
	{
		public View onCreatePreviewView(LayoutInflater inflater,
				ViewGroup container);
		public void onPreviewChange(View preview, int value);
	}

	public interface SummaryListener
	{
		public CharSequence onSumamryChange(int value);
	}

	public static class DefaultPreviewListener implements PreviewListener
	{
		@Override
		public View onCreatePreviewView(LayoutInflater inflater,
										ViewGroup container)
		{
			return inflater.inflate(R.layout.util_seek_bar_pref_text_preview,
					container, false);
		}

		@Override
		public void onPreviewChange(View preview, int value)
		{
			TextView v = (TextView)preview;
			v.setText(Integer.toString(value));
		}
	}

	public class DefaultSummaryListener implements SummaryListener
	{
		@Override
		public CharSequence onSumamryChange(int value)
		{
			return getSummary();
		}
	}

	public SeekBarPreference(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initAttribute(attrs);
	}

	public SeekBarPreference(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initAttribute(attrs);
	}

	public void setPreviewListener(PreviewListener listener)
	{
		if (listener == null)
		{
			mPreviewListener = new DefaultPreviewListener();
		}
		else
		{
			mPreviewListener = listener;
		}
	}

	public void setSummaryListener(SummaryListener listener)
	{
		if (listener == null)
		{
			mSummaryListener = new DefaultSummaryListener();
		}
		else
		{
			mSummaryListener = listener;
		}
		if (mIsValueReady)
		{
			setSummary(mSummaryListener.onSumamryChange(mValue));
		}
	}

	@Override
	protected View onCreateDialogView()
	{
		ViewGroup container = (ViewGroup)super.onCreateDialogView();

		LayoutInflater inflater = LayoutInflater.from(getContext());
		View root = inflater.inflate(R.layout.util_seek_bar_pref, container);

		ViewGroup previewContainer = (ViewGroup)root.findViewById(
				R.id.preview_container);
		mPreviewView = mPreviewListener.onCreatePreviewView(inflater,
				previewContainer);
		previewContainer.addView(mPreviewView);

		mSeekBar = (SeekBar)root.findViewById(R.id.seek_bar);
		mSeekBar.setMax(mMax - mMin);
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
				{
					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{}

					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser)
					{
						int value = progressToValue(progress);
						mPreviewListener.onPreviewChange(mPreviewView, value);
					}
				});

		return root;
	}

	@Override
	protected void onBindDialogView(View view)
	{
		super.onBindDialogView(view);
		mSeekBar.setProgress(valueToProgress(mValue));
		mPreviewListener.onPreviewChange(mPreviewView, mValue);
	}

	@Override
	protected Parcelable onSaveInstanceState()
	{
		final Parcelable superState = super.onSaveInstanceState();
		if (isPersistent())
		{
			// No need to save instance state since it's persistent
			return superState;
		}

		final SavedState ss = new SavedState(superState);
		ss.setValue(mValue);
		return ss;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state)
	{
		if (state == null || !state.getClass().equals(SavedState.class))
		{
			// Didn't save state for us in onSaveInstanceState
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState ss = (SavedState)state;
		super.onRestoreInstanceState(ss.getSuperState());
		setPref(ss.getValue());
	}

	@Override
	protected void onDialogClosed(boolean positiveResult)
	{
		super.onDialogClosed(positiveResult);
		if (positiveResult)
		{
			int value = getCurrentValue();
			if (callChangeListener(value))
			{
				setPref(value);
			}
		}
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue)
	{
		setPref(restoreValue ? getPersistedInt(mValue) : (Integer)defaultValue);
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index)
	{
		return a.getInt(index, (mMin + mMax) / 2);
	}

	private static class SavedState extends BaseSavedState
	{
		@SuppressWarnings("unused")
		public static final Parcelable.Creator<SavedState> CREATOR =
				new Parcelable.Creator<SavedState>()
		{
			@Override
			public SavedState createFromParcel(Parcel in)
			{
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size)
			{
				return new SavedState[size];
			}
		};

		public SavedState(Parcel source)
		{
			super(source);
			mValue = source.readInt();
		}

		public SavedState(Parcelable superState)
		{
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			super.writeToParcel(dest, flags);
			dest.writeInt(mValue);
		}

		public void setValue(int value)
		{
			mValue = value;
		}

		public int getValue()
		{
			return mValue;
		}

		private int mValue;
	}

	private void initAttribute(AttributeSet attrs)
	{
		TypedArray ta = getContext().obtainStyledAttributes(attrs,
				R.styleable.SeekBarPreference);

		mMin = ta.getInt(R.styleable.SeekBarPreference_min, 0);
		mMax = ta.getInt(R.styleable.SeekBarPreference_max, 100);

		ta.recycle();
	}

	private void setPref(int value)
	{
		final boolean wasBlocking = shouldDisableDependents();

		mValue = value;
		mIsValueReady = true;
		persistInt(value);

		final boolean isBlocking = shouldDisableDependents();
		if (isBlocking != wasBlocking)
		{
			notifyDependencyChange(isBlocking);
		}

		setSummary(mSummaryListener.onSumamryChange(mValue));
	}

	private int getCurrentValue()
	{
		return progressToValue(mSeekBar.getProgress());
	}

    private int valueToProgress(int value)
    {
        return value - mMin;
    }

    private int progressToValue(int progress)
    {
        return progress + mMin;
    }

	private View mPreviewView;
	private SeekBar mSeekBar;

	private boolean mIsValueReady = false;
	private int mValue;

	private int mMin;
	private int mMax;

	private PreviewListener mPreviewListener = new DefaultPreviewListener();
	private SummaryListener mSummaryListener = new DefaultSummaryListener();
}
