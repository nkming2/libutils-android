/*
 * SimpleListItem2Holder.kt
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.widget

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class SimpleListItem2Holder(root: View)
		: RecyclerView.ViewHolder(root), View.OnClickListener
{
	interface OnItemClickListener
	{
		fun onItemClick(holder: SimpleListItem2Holder, position: Int)
	}

	companion object
	{
		fun getFactory() : (root: View, TextView) -> SimpleListItem2Holder
		{
			return { root, text -> SimpleListItem2Holder(root) }
		}

		fun getFactory(l: (holder: SimpleListItem2Holder, position: Int) -> Unit)
				: (root: View, text: TextView) -> SimpleListItem2Holder
		{
			return { root, text -> SimpleListItem2Holder(root, l) }
		}
	}

	constructor(root: View,
			l: (holder: SimpleListItem2Holder, position: Int) -> Unit)
			: this(root)
	{
		_listener = l
		root.setOnClickListener(this)
	}

	override fun onClick(v: View?)
	{
		_listener(this, position)
	}

	val text1: TextView = itemView.findViewById(android.R.id.text1) as TextView
	val text2: TextView = itemView.findViewById(android.R.id.text2) as TextView

	// This is a hack such that the lambda could be null
	private lateinit var _listener
			: (holder: SimpleListItem2Holder, position: Int) -> Unit
}
