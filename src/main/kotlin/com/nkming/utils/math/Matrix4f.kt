package com.nkming.utils.math

/**
 * A basic 4x4 matrix
 *
 * @param values An array with 16 elements, in row-major order
 * @param isCopy Store the ref of @a values if true, a copy otherwise
 */
class Matrix4f(values: FloatArray, isCopy: Boolean)
{
	companion object
	{
		public fun newIdentity(): Matrix4f
		{
			return Matrix4f()
		}
	}

	public constructor(values: FloatArray)
			: this(values, true)

	public constructor()
			: this(floatArrayOf(
					1f, 0f, 0f, 0f,
					0f, 1f, 0f, 0f,
					0f, 0f, 1f, 0f,
					0f, 0f, 0f, 1f), false)

	public operator fun get(y: Int, x: Int): Float
	{
		return values[y * 4 + x]
	}

	public operator fun set(y: Int, x: Int, v: Float)
	{
		values[y * 4 + x] = v
	}

	public operator fun times(rhs: Matrix4f): Matrix4f
	{
		val product = FloatArray(16)
		for (y in 0..3)
		{
			for (x in 0..3)
			{
				product[y * 4 + x] = this[y, 0] * rhs[0, x] +
						this[y, 1] * rhs[1, x] + this[y, 2] * rhs[2, x] +
						this[y, 3] * rhs[3, x]
			}
		}
		return Matrix4f(product, false)
	}

	public operator fun times(rhs: Vector4f): Vector4f
	{
		val product = FloatArray(4)
		for (y in 0..3)
		{
			product[y] = this[y, 0] * rhs[0] + this[y, 1] * rhs[1] +
					this[y, 2] * rhs[2] + this[y, 3] * rhs[3]
		}
		return Vector4f(product, false)
	}

	public override fun toString(): String
	{
		return "[[${values.slice(0..3).joinToString()}], " +
				"[${values.slice(4..7).joinToString()}], " +
				"[${values.slice(8..11).joinToString()}], " +
				"[${values.slice(12..15).joinToString()}]]"
	}

	private val values: FloatArray

	init
	{
		this.values = if (isCopy) values.clone() else values
		this.values.size == 4 * 4 || throw IllegalArgumentException(
				"Expect 16 elements")
	}
}
