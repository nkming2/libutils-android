package com.nkming.utils.widget

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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

	val text1: TextView = itemView.findViewById(android.R.id.text1)
	val text2: TextView = itemView.findViewById(android.R.id.text2)

	private val _listener: ((holder: SimpleListItem2Holder, position: Int) ->
			Unit)? = onClickListener

	init
	{
		root.setOnClickListener(if (_listener != null) this else null)
	}
}
