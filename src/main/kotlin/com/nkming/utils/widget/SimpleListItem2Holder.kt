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

class SimpleListItem2Holder(root: View,
		onClickListener: ((holder: SimpleListItem2Holder, position: Int) -> Unit)?
				= null)
		: RecyclerView.ViewHolder(root), View.OnClickListener
{
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

	override fun onClick(v: View?)
	{
		// Normally won't be null, just play safe
		_listener?.invoke(this, position)
	}

	val text1: TextView = itemView.findViewById(android.R.id.text1) as TextView
	val text2: TextView = itemView.findViewById(android.R.id.text2) as TextView

	private val _listener: ((holder: SimpleListItem2Holder, position: Int) ->
			Unit)? = onClickListener

	init
	{
		root.setOnClickListener(if (_listener != null) this else null)
	}
}
