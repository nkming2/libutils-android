package com.nkming.utils.graphic;

import com.nkming.utils.type.Size;

/**
 * SizeCalc implementation that does nothing. Always return the adjustSize when
 * calc() is called
 */
public class NullSizeCalc extends SizeCalc
{
	@Override
	public Size calc(Size adjustSize, Size targetSize)
	{
		return adjustSize;
	}
}
