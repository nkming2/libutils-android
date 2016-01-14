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

// Point
operator fun Point.plus(rhs: Point): Point
{
	return Point(this.x + rhs.x, this.y + rhs.y)
}

operator fun Point.minus(rhs: Point): Point
{
	return Point(this.x - rhs.x, this.y - rhs.y)
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
