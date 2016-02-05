/*
 * ArrayRecyclerAdapter.kt
 *
 * Author: Ming Tsang
 * Copyright (c) 2015 Ming Tsang
 * Refer to LICENSE for details
 */

package com.nkming.utils.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nkming.utils.Log
import java.util.*

/**
 * Derived class of RecyclerView.Adapter that looks similar to the original
 * ArrayAdapter. Filter is not supported
 */
class ArrayRecyclerAdapter<_ItemT, _ViewHolderT>(
		context: Context, resource: Int, textViewResourceId: Int,
		objects: MutableList<_ItemT>,
		factory: (root: View, text: TextView) -> _ViewHolderT)
		: RecyclerView.Adapter<_ViewHolderT>()
		where _ViewHolderT: RecyclerView.ViewHolder,
				_ViewHolderT: ArrayRecyclerAdapter.ViewHolderAdapter
{
	companion object
	{
		fun <_ViewHolderT> createFromResource(context: Context,
				textArrayResId: Int, textViewResId: Int,
				factory: (root: View, text: TextView) -> _ViewHolderT)
				: ArrayRecyclerAdapter<CharSequence, _ViewHolderT>
				where _ViewHolderT: RecyclerView.ViewHolder,
						_ViewHolderT: ArrayRecyclerAdapter.ViewHolderAdapter
		{
			val strings: Array<CharSequence> = context.resources.getTextArray(
					textArrayResId)
			return ArrayRecyclerAdapter(context, textViewResId, strings, factory)
		}

		private /* XXX const */ val LOG_TAG = ArrayRecyclerAdapter::class.java.canonicalName
	}

	interface ViewHolderAdapter
	{
		fun setText(text: CharSequence)
	}

	constructor(context: Context, resource: Int,
			factory: (root: View, text: TextView) -> _ViewHolderT)
			: this(context, resource, 0, ArrayList<_ItemT>(), factory)

	constructor(context: Context, resource: Int, textViewResourceId: Int,
			factory: (root: View, text: TextView) -> _ViewHolderT)
			: this(context, resource, textViewResourceId, ArrayList<_ItemT>(),
			factory)

	constructor(context: Context, resource: Int, objects: Array<_ItemT>,
			factory: (root: View, text: TextView) -> _ViewHolderT)
			: this(context, resource, 0, objects.toMutableList(), factory)

	constructor(context: Context, resource: Int, textViewResourceId: Int,
			objects: Array<_ItemT>,
			factory: (root: View, text: TextView) -> _ViewHolderT)
			: this(context, resource, textViewResourceId, objects.toMutableList(),
			factory)

	constructor(context: Context, resource: Int, objects: MutableList<_ItemT>,
			factory: (root: View, text: TextView) -> _ViewHolderT)
			: this(context, resource, 0, objects, factory)

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
			: _ViewHolderT
	{
		try
		{
			val root = _inflater.inflate(_resource, parent, false)
			val text = (if (_textId == 0) root else root.findViewById(_textId))
					as TextView
			return _factory(root, text)
		}
		catch (e: ClassCastException)
		{
			Log.e(LOG_TAG, "You must supply a resource ID for a TextView")
			throw IllegalStateException(
					"ArrayRecyclerAdapter requires the resource ID to be a TextView",
					e)
		}
	}

	override fun onBindViewHolder(holder: _ViewHolderT, position: Int)
	{
		val item = _objects[position]
		if (item is CharSequence)
		{
			holder.setText(item)
		}
		else
		{
			holder.setText(item.toString())
		}
	}

	override fun getItemCount(): Int = _objects.size

	fun add(obj: _ItemT)
	{
		synchronized(_objects)
		{
			_objects.add(obj)
		}
		if (notifyOnChange)
		{
			notifyItemInserted(_objects.size - 1)
		}
	}

	fun addAll(vararg obj: _ItemT)
	{
		var beg: Int = 0
		synchronized(_objects)
		{
			beg = _objects.size
			_objects.addAll(obj)
		}
		if (notifyOnChange)
		{
			notifyItemRangeInserted(beg, obj.size)
		}
	}

	fun insert(obj: _ItemT, position: Int)
	{
		synchronized(_objects)
		{
			_objects.add(position, obj)
		}
		if (notifyOnChange)
		{
			notifyItemInserted(position)
		}
	}

	fun remove(obj: _ItemT)
	{
		var position: Int = 0
		synchronized(_objects)
		{
			position = _objects.indexOf(obj)
			if (position == -1)
			{
				return
			}
			_objects.removeAt(position)
		}
		if (notifyOnChange)
		{
			notifyItemRemoved(position)
		}
	}

	fun clear()
	{
		var size: Int = 0
		synchronized(_objects)
		{
			size = _objects.size
			_objects.clear()
		}
		if (notifyOnChange)
		{
			notifyItemRangeRemoved(0, size)
		}
	}

	fun sort(comp: Comparator<in _ItemT>)
	{
		synchronized(_objects)
		{
			Collections.sort(_objects, comp)
		}
		if (notifyOnChange)
		{
			notifyDataSetChanged()
		}
	}

	/**
	 * Similar to ArrayAdapter#setNotifyOnChange() with one exception: calling
	 * notifyDataSetChanged() would NOT reset the flag to true
	 */
	var notifyOnChange: Boolean = true

	val context: Context
		get() = _context

	val count: Int
		get() = _objects.size

	private val _context = context
	private val _inflater = LayoutInflater.from(_context)
	private val _resource = resource
	private val _objects = objects
	private val _textId = textViewResourceId
	private val _factory = factory
}
