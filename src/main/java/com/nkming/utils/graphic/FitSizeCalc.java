package com.nkming.utils.graphic;

import com.nkming.utils.type.Size;

/**
 * SizeCalc implementation that tries to fit into the target without oversizing
 * in either dimension
 */
public class FitSizeCalc extends SizeCalc
{
	@Override
	public Size calc(Size adjustSize, Size targetSize)
	{
		final float ratioX = adjustSize.w() / (float)targetSize.w();
		final float ratioY = adjustSize.h() / (float)targetSize.h();
		final float useRatio = Math.max(ratioX, ratioY);

		if (useRatio < 1.0f && !isAllowUpscale())
		{
			return adjustSize;
		}
		else
		{
			return new Size((int)(adjustSize.w() / useRatio),
					(int)(adjustSize.h() / useRatio));
		}
	}
}
