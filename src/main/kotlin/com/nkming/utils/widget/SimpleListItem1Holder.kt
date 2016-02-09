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

class SimpleListItem1Holder(root: View,
		onClickListener: ((holder: SimpleListItem1Holder, position: Int) -> Unit)?
				= null)
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

	override fun onClick(v: View?)
	{
		// Normally won't be null, just play safe
		_listener?.invoke(this, position)
	}

	override fun setText(text: CharSequence)
	{
		text1.text = text
	}

	val text1: TextView
		get() = itemView as TextView

	private val _listener: ((holder: SimpleListItem1Holder, position: Int) ->
			Unit)? = onClickListener

	init
	{
		root.setOnClickListener(if (_listener != null) this else null)
	}
}
