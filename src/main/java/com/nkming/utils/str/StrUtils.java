/*
 * StrUtils.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.str;

import java.util.Collection;

public class StrUtils
{
	/**
	 * Join multiple objects with toString(). @a glue will be inserted between
	 * each objects
	 *
	 * @param glue
	 * @param c
	 * @return
	 */
	public static <T> String Implode(String glue, Collection<T> c)
	{
		String product = null;
		for (T obj : c)
		{
			if (product == null)
			{
				product = obj.toString();
			}
			else
			{
				product += glue + obj.toString();
			}
		}
		return product;
	}
}
