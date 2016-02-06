/*
 * SimpleListItem1Holder.kt
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.widget

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class SimpleListItem1Holder(root: View)
		: RecyclerView.ViewHolder(root), ArrayRecyclerAdapter.ViewHolderAdapter,
				View.OnClickListener
{
	companion object
	{
		fun getFactory() : (root: View, text: TextView) -> SimpleListItem1Holder
		{
			return { root, text -> SimpleListItem1Holder(root) }
		}

		fun getFactory(l: (holder: SimpleListItem1Holder, position: Int) -> Unit)
				: (root: View, text: TextView) -> SimpleListItem1Holder
		{
			return { root, text -> SimpleListItem1Holder(root, l) }
		}
	}

	constructor(root: View,
			l: (holder: SimpleListItem1Holder, position: Int) -> Unit)
			: this(root)
	{
		_listener = l
		root.setOnClickListener(this)
	}

	override fun onClick(v: View?)
	{
		_listener(this, position)
	}

	override fun setText(text: CharSequence)
	{
		text1.text = text
	}

	val text1: TextView
		get() = itemView as TextView

	// This is a hack such that the lambda could be null
	private lateinit var _listener
			: (holder: SimpleListItem1Holder, position: Int) -> Unit
}
