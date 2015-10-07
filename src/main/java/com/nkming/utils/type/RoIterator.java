/*
 * RoIterator.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.type;

import java.util.Iterator;

/**
 * Hack in case remove is not needed and want to save some lines
 *
 * @param <T>
 */
public abstract class RoIterator<T> implements Iterator<T>
{
	@Override
	public final void remove()
	{
		throw new UnsupportedOperationException();
	}
}
