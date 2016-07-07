package com.nkming.utils.widget.decorator

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nkming.utils.type.ext.childIterator

/**
 * Add a footer view to a RecyclerList. Horizontal list is unsupported. Grid
 * layout is unsupported
 */
class FooterDecoration(view: View, orientation: Int)
		: RecyclerView.ItemDecoration()
{
	companion object
	{
		const val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
		const val VERTICAL_LIST = LinearLayoutManager.VERTICAL
	}

	override fun onDraw(c: Canvas, parent: RecyclerView,
			state: RecyclerView.State)
	{
		for (child in parent.childIterator())
		{
			if (parent.getChildAdapterPosition(child)
					== parent.adapter.itemCount - 1)
			{
				_view.layout(parent.left, parent.bottom - _view.measuredHeight,
						parent.right, parent.bottom)
				val scrollOffset = parent.bottom - child.bottom
				c.save()
				c.translate(0f, parent.bottom.toFloat())
				c.translate(0f, -scrollOffset.toFloat())
				_view.draw(c)
				c.restore()
			}
		}
	}

	override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
			state: RecyclerView.State)
	{
		if (parent.getChildAdapterPosition(view) != parent.adapter.itemCount - 1)
		{
			outRect.setEmpty()
			return
		}

		if (_view.measuredHeight == 0)
		{
			val heightSpec = when (_view.layoutParams.height)
			{
				ViewGroup.LayoutParams.MATCH_PARENT ->
				{
					View.MeasureSpec.makeMeasureSpec(parent.height,
							View.MeasureSpec.EXACTLY)
				}

				ViewGroup.LayoutParams.WRAP_CONTENT ->
				{
					// FIXME This probably is broken
					View.MeasureSpec.makeMeasureSpec(parent.height,
							View.MeasureSpec.AT_MOST)
				}

				else ->
				{
					View.MeasureSpec.makeMeasureSpec(_view.layoutParams.height,
							View.MeasureSpec.EXACTLY)
				}
			}
			_view.measure(View.MeasureSpec.makeMeasureSpec(parent.measuredWidth,
					View.MeasureSpec.AT_MOST), heightSpec)
		}
		outRect.set(0, 0, 0, _view.measuredHeight)
	}

	private val _view = view
	private val _orientation = orientation

	init
	{
		if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST)
		{
			throw IllegalArgumentException("invalid orientation")
		}
		if (orientation == HORIZONTAL_LIST)
		{
			throw IllegalArgumentException("horizontal not yet supported")
		}
	}
}
