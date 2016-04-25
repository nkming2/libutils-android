/*
 * Extension.kt
 * Extensions for various common classes in Android
 */

package com.nkming.utils.type.ext

import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF

// Point
operator fun Point.plus(rhs: Point): Point
{
	val product = Point(this)
	product += rhs
	return product
}

operator fun Point.plusAssign(rhs: Point)
{
	this.x += rhs.x
	this.y += rhs.y
}

operator fun Point.minus(rhs: Point): Point
{
	val product = Point(this)
	product -= rhs
	return product
}

operator fun Point.minusAssign(rhs: Point)
{
	this.x -= rhs.x
	this.y -= rhs.y
}

operator fun Point.times(rhs: Float): Point
{
	val product = Point(this)
	product *= rhs
	return product
}

operator fun Point.timesAssign(rhs: Float)
{
	this.x = (this.x * rhs).toInt()
	this.y = (this.y * rhs).toInt()
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
	val product = PointF(this.x, this.y)
	product += rhs
	return product
}

operator fun PointF.plus(rhs: Point): PointF
{
	val product = PointF(this.x, this.y)
	product += rhs
	return product
}

operator fun PointF.plusAssign(rhs: PointF)
{
	this.x += rhs.x
	this.y += rhs.y
}

operator fun PointF.plusAssign(rhs: Point)
{
	this.x += rhs.x
	this.y += rhs.y
}

operator fun PointF.minus(rhs: PointF): PointF
{
	val product = PointF(this.x, this.y)
	product -= rhs
	return product
}

operator fun PointF.minus(rhs: Point): PointF
{
	val product = PointF(this.x, this.y)
	product -= rhs
	return product
}

operator fun PointF.minusAssign(rhs: PointF)
{
	this.x -= rhs.x
	this.y -= rhs.y
}

operator fun PointF.minusAssign(rhs: Point)
{
	this.x -= rhs.x
	this.y -= rhs.y
}

operator fun PointF.times(rhs: Float): PointF
{
	val product = PointF(this.x, this.y)
	product *= rhs
	return product
}

operator fun PointF.timesAssign(rhs: Float)
{
	this.x *= rhs
	this.y *= rhs
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
