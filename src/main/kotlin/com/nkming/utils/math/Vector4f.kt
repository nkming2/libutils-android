package com.nkming.utils.math

/**
 * A basic 4x1 vector
 *
 * @param values An array with 4 elements
 * @param isCopy Store the ref of @a values if true, a copy otherwise
 */
class Vector4f(values: FloatArray, isCopy: Boolean)
{
	public constructor(values: FloatArray)
			: this(values, true)

	public constructor()
			: this(floatArrayOf(0f, 0f, 0f, 0f), false)

	public operator fun get(x: Int): Float
	{
		return values[x]
	}

	public operator fun set(x: Int, v: Float)
	{
		values[x] = v
	}

	public fun dot(rhs: Vector4f): Float
	{
		return this[0] * rhs[0] + this[1] * rhs[1] + this[2] * rhs[2] +
				this[3] * rhs[3]
	}

	public operator fun times(rhs: Vector4f): Float
	{
		return this.dot(rhs)
	}

	public operator fun times(rhs: Matrix4f): Vector4f
	{
		val product = FloatArray(4)
		for (x in 0..3)
		{
			product[x] = this[0] * rhs[0, x] + this[1] * rhs[1, x] +
					this[2] * rhs[2, x] + this[3] * rhs[3, x]
		}
		return Vector4f(product, false)
	}

	public operator fun times(rhs: Float): Vector4f
	{
		return Vector4f(floatArrayOf(values[0] * rhs, values[1] * rhs,
				values[2] * rhs, values[3] * rhs), false)
	}

	public override fun toString(): String
	{
		return "[${values.joinToString()}]"
	}

	private val values: FloatArray

	init
	{
		this.values = if (isCopy) values.clone() else values
		this.values.size == 4 || throw IllegalArgumentException(
				"Expect 4 elements")
	}
}
