/*
 * Extension.kt
 * Extensions for various common classes in Android
 *
 * Copyright (c) 2016 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.type.ext

import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF

// Point
operator fun Point.plus(rhs: Point): Point
{
	return Point(this.x + rhs.x, this.y + rhs.y)
}

operator fun Point.minus(rhs: Point): Point
{
	return Point(this.x - rhs.x, this.y - rhs.y)
}

val Point.length: Double
	get()
	{
		return Math.sqrt(Math.pow(this.x.toDouble(), 2.0)
				+ Math.pow(this.y.toDouble(), 2.0))
	}

fun Point.dist(to: Point): Double
{
	return (this - to).length
}

// PointF
operator fun PointF.plus(rhs: PointF): PointF
{
	return PointF(this.x + rhs.x, this.y + rhs.y)
}

operator fun PointF.plus(rhs: Point): PointF
{
	return PointF(this.x + rhs.x, this.y + rhs.y)
}

operator fun PointF.minus(rhs: PointF): PointF
{
	return PointF(this.x - rhs.x, this.y - rhs.y)
}

operator fun PointF.minus(rhs: Point): PointF
{
	return PointF(this.x - rhs.x, this.y - rhs.y)
}

val PointF.length: Double
	get()
	{
		return Math.sqrt(Math.pow(this.x.toDouble(), 2.0)
				+ Math.pow(this.y.toDouble(), 2.0))
	}

fun PointF.dist(to: Point): Double
{
	return (this - to).length
}

fun PointF.dist(to: PointF): Double
{
	return (this - to).length
}

// Rect
operator fun Rect.plus(move: Point): Rect
{
	return Rect(this.left + move.x, this.top + move.y,
			this.right + move.x, this.bottom + move.y)
}

operator fun Rect.minus(move: Point): Rect
{
	return Rect(this.left - move.x, this.top - move.y,
			this.right - move.x, this.bottom - move.y)
}

// RectF
operator fun RectF.plus(move: PointF): RectF
{
	return RectF(this.left + move.x, this.top + move.y,
			this.right + move.x, this.bottom + move.y)
}

operator fun RectF.plus(move: Point): RectF
{
	return RectF(this.left + move.x, this.top + move.y,
			this.right + move.x, this.bottom + move.y)
}

operator fun RectF.minus(move: PointF): RectF
{
	return RectF(this.left - move.x, this.top - move.y,
			this.right - move.x, this.bottom - move.y)
}

operator fun RectF.minus(move: Point): RectF
{
	return RectF(this.left - move.x, this.top - move.y,
			this.right - move.x, this.bottom - move.y)
}
