/*
 * FillSizeCalc.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.graphic;

import com.nkming.utils.type.Size;

/**
 * SizeCalc implementation that tries to fill the entire target region, even
 * with one of the dimension oversizing
 */
public class FillSizeCalc extends SizeCalc
{
	@Override
	public Size calc(Size adjustSize, Size targetSize)
	{
		final float ratioX = adjustSize.w() / (float)targetSize.w();
		final float ratioY = adjustSize.h() / (float)targetSize.h();
		final float useRatio = Math.min(ratioX, ratioY);

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
