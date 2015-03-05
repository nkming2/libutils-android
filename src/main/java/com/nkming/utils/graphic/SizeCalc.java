/*
 * SizeCalc.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.graphic;

import com.nkming.utils.type.Size;

public abstract class SizeCalc
{
	public abstract Size calc(Size adjustSize, Size targetSize);

	/**
	 * Set whether to allow upscaling. If not, then the original size will be
	 * returned if it's smaller than the target size. Default is true
	 *
	 * @param flag
	 */
	public void setAllowUpscale(boolean flag)
	{
		mIsAllowUpscale = flag;
	}

	public boolean isAllowUpscale()
	{
		return mIsAllowUpscale;
	}

	private boolean mIsAllowUpscale = true;
}
