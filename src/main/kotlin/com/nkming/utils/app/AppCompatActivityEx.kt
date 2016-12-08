package com.nkming.utils.app

import android.support.v7.app.AppCompatActivity

open class AppCompatActivityEx : AppCompatActivity()
{
	override fun onBackPressed()
	{
		if (dispatch{it.onBackPressed()})
		{
			return
		}
		super.onBackPressed()
	}

	override fun onWindowFocusChanged(hasFocus: Boolean)
	{
		super.onWindowFocusChanged(hasFocus)
		dispatch1Way{it.onWindowFocusChanged(hasFocus)}
	}

	/**
	 * Dispatch to suitable child fragments
	 *
	 * @l What to do when a suitable fragment child is found. Return true to
	 * break the loop
	 * @return True if @a l returned true, false otherwise
	 */
	private fun dispatch(l: (FragmentEx) -> Boolean): Boolean
	{
		for (f in supportFragmentManager.fragments ?: listOf())
		{
			if (f != null && f.isAdded && f is FragmentEx)
			{
				if (l(f))
				{
					return true
				}
			}
		}
		return false
	}

	private fun dispatch1Way(l: (FragmentEx) -> Unit)
	{
		dispatch(
		{
			l(it)
			false
		})
	}
}
