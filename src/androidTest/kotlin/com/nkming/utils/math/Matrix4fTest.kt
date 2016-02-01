package com.nkming.utils.math

import org.junit.Assert.*
import org.junit.Test

class Matrix4fTest
{
	@Test
	public fun constructor()
	{
		val values = newMatrixArray()
		val mat1 = Matrix4f(values)
		for (y in 0..3)
		{
			for (x in 0..3)
			{
				assertEquals(values[y * 4 + x], mat1[y, x], .0001f)
			}
		}
		mat1[0, 0] = 9.5f
		assertNotEquals(values[0], mat1[0, 0], .0001f)

		val mat2 = Matrix4f(values, false)
		mat2[0, 0] = 9.5f
		assertEquals(values[0], mat2[0, 0], .0001f)
	}

	@Test
	public fun timesVector4f()
	{
		val mat = Matrix4f(floatArrayOf(
				4f, 5f, 1f, 8f,
				0f, 3f, 6f, 1f,
				3f, 5f, 0f, 9f,
				2f, 4f, 6f, 1f), false)
		val vec = Vector4f(floatArrayOf(1f, 2f, 3f, 4f), false)
		val result = mat * vec
		assertEquals(49f, result[0], .0001f)
		assertEquals(28f, result[1], .0001f)
		assertEquals(49f, result[2], .0001f)
		assertEquals(32f, result[3], .0001f)
	}

	@Test
	public fun toString_()
	{
		val mat1 = Matrix4f(newMatrixArray())
		assertEquals("[[0.1, 0.2, 0.3, 0.4], [1.1, 1.2, 1.3, 1.4], [2.1, 2.2, 2.3, 2.4], [3.1, 3.2, 3.3, 3.4]]",
				mat1.toString())
	}

	private fun newMatrixArray(): FloatArray
	{
		return floatArrayOf(.1f, .2f, .3f, .4f,
			1.1f, 1.2f, 1.3f, 1.4f,
			2.1f, 2.2f, 2.3f, 2.4f,
			3.1f, 3.2f, 3.3f, 3.4f)
	}
}
