/*
 * StatUtils.java
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.statistic;

import java.util.Iterator;

public class StatUtils
{
	public static <T extends Number> double mean(Iterator<T> data)
	{
		double sum = .0;
		int count = 0;
		while (data.hasNext())
		{
			sum += data.next().doubleValue();
			++count;
		}
		return (count > 0) ? sum / count : 0;
	}

	/**
	 * Calculating the unbiased sample variance in one pass
	 *
	 * @param data
	 * @param <T>
	 * @return
	 */
	public static <T extends Number> double variance(Iterator<T> data)
	{
		double sum1 = .0;
		double sum2 = .0;
		int count = 0;
		while (data.hasNext())
		{
			double val = data.next().doubleValue();
			sum1 += val * val;
			sum2 += val;
			++count;
		}
		if (count == 0)
		{
			return .0;
		}

		final double mean = sum2 / count;
		return (sum1 - (count * mean * mean)) / (count - 1);
	}

	/**
	 * Calculating the corrected sample standard deviation in one pass
	 *
	 * @param data
	 * @param <T>
	 * @return
	 */
	public static <T extends Number> double stdev(Iterator<T> data)
	{
		return Math.sqrt(variance(data));
	}

	private StatUtils()
	{}
}
