package com.nkming.utils.app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray
import android.view.View
import kotlin.reflect.KProperty

open class FragmentEx : Fragment()
{
	interface LazyView<T : View>
	{
		operator fun getValue(thisRef: Any?, property: KProperty<*>): T

		operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
		{
			throw UnsupportedOperationException()
		}
	}

	interface ViewAwareLazy<T : Any>
	{
		operator fun getValue(thisRef: Any?, property: KProperty<*>): T

		operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
		{
			throw UnsupportedOperationException()
		}

		fun uninit()
	}

	open fun onBackPressed(): Boolean
	{
		return false
	}

	override fun onActivityCreated(savedInstanceState: Bundle?)
	{
		super.onActivityCreated(savedInstanceState)
		_views.clear()
		for (l in _viewAwareLazies)
		{
			l.uninit()
		}
	}

	/**
	 * Lazily find a view, and cache the result for later use. The different
	 * between this method and doing it through lazy delegate, is that we would
	 * handle the view recreation for you (eg, after your fragment coming back
	 * from the back stack). This method is not thread-safe
	 *
	 * @param id View id
	 * @return The view or null
	 */
	fun <T : View> findViewOrNull(id: Int): T?
	{
		val index = _views.indexOfKey(id)
		if (index >= 0)
		{
			return _views.valueAt(index) as T?
		}
		else
		{
			val v = view?.findViewById(id)
			_views.put(id, v)
			return v as T?
		}
	}

	/**
	 * Same as findViewOrNull() but will throw instead of returning null
	 *
	 * @param id
	 * @return
	 * @see findViewOrNull()
	 */
	fun <T : View> findView(id: Int): T
	{
		return findViewOrNull(id)!!
	}

	/**
	 * Delegates that would refind the view if the view is recreated
	 *
	 * @param id
	 * @return
	 */
	protected fun <T : View> lazyView(id: Int): LazyView<T>
	{
		return LazyViewImpl(id)
	}

	/**
	 * Delegates that would rerun the initializer if the view is recreated
	 *
	 * @param initializer
	 * @return
	 */
	protected fun <T : Any> viewAwareLazy(initializer: () -> T): ViewAwareLazy<T>
	{
		val inst = ViewAwareLazyImpl(initializer)
		_viewAwareLazies.add(inst)
		return inst
	}

	private class LazyViewImpl<T : View>(id: Int) : LazyView<T>
	{
		override operator fun getValue(thisRef: Any?, property: KProperty<*>): T
		{
			return (thisRef as FragmentEx).findView(_id)
		}

		private val _id = id
	}

	private class ViewAwareLazyImpl<T : Any>(initializer: () -> T)
			: ViewAwareLazy<T>
	{
		override operator fun getValue(thisRef: Any?, property: KProperty<*>): T
		{
			if (!_hasInitialized)
			{
				synchronized(this)
				{
					_value = getValue_()
				}
			}
			return _value
		}

		override fun uninit()
		{
			_hasInitialized = false
		}

		private fun getValue_(): T
		{
			if (!_hasInitialized)
			{
				_value = _initializer()
				_hasInitialized = true
			}
			return _value
		}

		private val _initializer = initializer
		private var _hasInitialized = false
		private lateinit var _value: T
	}

	private val _views = SparseArray<View?>()
	private val _viewAwareLazies = arrayListOf<ViewAwareLazy<out Any>>()
}
