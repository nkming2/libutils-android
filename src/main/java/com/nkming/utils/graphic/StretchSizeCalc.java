package com.nkming.utils.graphic;

import com.nkming.utils.type.Size;

/**
 * SizeCalc implementation that stretches both dimension to match the target
 */
public class StretchSizeCalc extends SizeCalc
{
	@Override
	public Size calc(Size adjustSize, Size targetSize)
	{
		if (adjustSize.w() < targetSize.w() && adjustSize.h() < targetSize.h()
				&& !isAllowUpscale())
		{
			return adjustSize;
		}
		else
		{
			return targetSize;
		}
	}
}
