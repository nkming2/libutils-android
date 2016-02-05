package com.nkming.utils.type.ext

import android.graphics.Point
import org.junit.Assert.*
import org.junit.Test

class ExtensionTest
{
	@Test
	fun PointPlus()
	{
		val sbj = Point(1, 0) + Point(5, -2)
		assertEquals(6, sbj.x)
		assertEquals(-2, sbj.y)
	}

	@Test
	fun PointMinus()
	{
		val sbj = Point(1, 0) - Point(5, -2)
		assertEquals(-4, sbj.x)
		assertEquals(2, sbj.y)
	}

	@Test
	fun PointTimes()
	{
		val sbj = Point(2, -4) * 3f
		assertEquals(6, sbj.x)
		assertEquals(-12, sbj.y)
	}

	@Test
	fun PointLength()
	{
		assertEquals(10.0, Point(6, 8).length, .0001)
	}

	@Test
	fun PointDist()
	{
		val obj1 = Point(1, 1)
		val obj2 = Point(5, 3)
		assertEquals(Math.sqrt(20.0), obj1.dist(obj2), .0001)
	}
}
