package com.nkming.utils.type;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorComposite<T> implements Iterator<T>
{
	public IteratorComposite(Iterator<T> a, Iterator<T> b)
	{
		mItA = a;
		mItB = b;
		mItActive = mItA;
	}

	@Override
	public boolean hasNext()
	{
		return (mItA.hasNext() || mItB.hasNext());
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
			if (mItA.hasNext())
			{
				mItActive = mItA;
				return mItA.next();
			}
			else
			{
				mItActive = mItB;
				return mItB.next();
			}
		}
	}

	@Override
	public void remove()
	{
		mItActive.remove();
	}

	private Iterator<T> mItA;
	private Iterator<T> mItB;
	private Iterator<T> mItActive;
}
