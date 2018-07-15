package com.nkming.utils.widget

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
