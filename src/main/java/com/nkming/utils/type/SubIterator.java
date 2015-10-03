/*
 * SubIterator.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.type;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An Iterator that iterates a subset of another Iterator. For List, consider
 * List#subList instead
 *
 * @param <T>
 */
public class SubIterator<T> implements Iterator<T>
{
	/**
	 * Create an Iterator that iterate between [start, end) in @a it
	 *
	 * @param it
	 * @param start
	 * @param end
	 */
	public SubIterator(Iterator<T> it, int start, int end)
	{
		mIt = it;
		for (int i = 0; i < start && mIt.hasNext(); ++i)
		{
			mIt.next();
		}
		mLeft = end - start;
	}

	@Override
	public boolean hasNext()
	{
		return (mIt.hasNext() && mLeft > 0);
	}

	@Override
	public T next()
	{
		if (!hasNext())
		{
			throw new NoSuchElementException();
		}
		else
		{
			--mLeft;
			return mIt.next();
		}
	}

	@Override
	public void remove()
	{
		mIt.remove();
	}

	private Iterator<T> mIt;
	private int mLeft;
}
