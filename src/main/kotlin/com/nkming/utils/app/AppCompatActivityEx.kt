package com.nkming.utils.app

import android.support.v7.app.AppCompatActivity

open class AppCompatActivityEx : AppCompatActivity()
{
	override fun onBackPressed()
	{
		for (f in supportFragmentManager.fragments)
		{
			if (f != null && !f.isDetached && f is FragmentEx)
			{
				if (f.onBackPressed())
				{
					return
				}
			}
		}
		super.onBackPressed()
	}
}
