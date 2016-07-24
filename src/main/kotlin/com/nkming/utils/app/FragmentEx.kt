package com.nkming.utils.app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray
import android.view.View
import java.util.*
import kotlin.reflect.KProperty

open class FragmentEx : Fragment()
{
	open fun onBackPressed(): Boolean
	{
		for (f in childFragmentManager.fragments ?: listOf())
		{
			if (f != null && f.isAdded && f is FragmentEx)
			{
				if (f.onBackPressed())
				{
					return true
				}
			}
		}
		return false
	}

	open fun onWindowFocusChanged(hasFocus: Boolean)
	{
		for (f in childFragmentManager.fragments ?: listOf())
		{
			if (f != null && f.isAdded && f is FragmentEx)
			{
				f.onWindowFocusChanged(hasFocus)
			}
		}
	}

	override fun onActivityCreated(savedInstanceState: Bundle?)
	{
		super.onActivityCreated(savedInstanceState)
		_viewAwareImpl.onActivityCreated(savedInstanceState)
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
		return _viewAwareImpl.findViewOrNull(id)
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
		return _viewAwareImpl.findView(id)
	}

	/**
	 * Delegates that would refind the view if the view is recreated
	 *
	 * @param id
	 * @return
	 */
	protected fun <T : View> lazyView(id: Int): FragmentViewAwareImpl.LazyView<T>
	{
		return _viewAwareImpl.lazyView(id)
	}

	/**
	 * Delegates that would rerun the initializer if the view is recreated
	 *
	 * @param initializer
	 * @return
	 */
	protected fun <T : Any?> viewAwareLazy(initializer: () -> T)
			: FragmentViewAwareImpl.ViewAwareLazy<T>
	{
		return _viewAwareImpl.viewAwareLazy(initializer)
	}

	private val _viewAwareImpl = object: FragmentViewAwareImpl()
	{
		override fun getView(): View?
		{
			return view
		}
	}
}

open class NativeFragmentEx : android.app.Fragment()
{
	override fun onActivityCreated(savedInstanceState: Bundle?)
	{
		super.onActivityCreated(savedInstanceState)
		_viewAwareImpl.onActivityCreated(savedInstanceState)
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
		return _viewAwareImpl.findViewOrNull(id)
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
		return _viewAwareImpl.findView(id)
	}

	/**
	 * Delegates that would refind the view if the view is recreated
	 *
	 * @param id
	 * @return
	 */
	protected fun <T : View> lazyView(id: Int): FragmentViewAwareImpl.LazyView<T>
	{
		return _viewAwareImpl.lazyView(id)
	}

	/**
	 * Delegates that would rerun the initializer if the view is recreated
	 *
	 * @param initializer
	 * @return
	 */
	protected fun <T : Any?> viewAwareLazy(initializer: () -> T)
			: FragmentViewAwareImpl.ViewAwareLazy<T>
	{
		return _viewAwareImpl.viewAwareLazy(initializer)
	}

	private val _viewAwareImpl = object: FragmentViewAwareImpl()
	{
		override fun getView(): View?
		{
			return view
		}
	}
}

/**
 * The logic block used to build a fragment aware of view changes and reset lazy
 * fields correspondingly. FragmentEx is a sample implementation that adapted
 * this class
 */
abstract class FragmentViewAwareImpl
{
	interface LazyView<T : View>
	{
		operator fun getValue(thisRef: Any?, property: KProperty<*>): T

		operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
		{
			throw UnsupportedOperationException()
		}
	}

	interface ViewAwareLazy<T : Any?>
	{
		operator fun getValue(thisRef: Any?, property: KProperty<*>): T

		operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
		{
			throw UnsupportedOperationException()
		}

		fun uninit()
	}

	fun onActivityCreated(savedInstanceState: Bundle?)
	{
		_views.clear()
		for (l in _viewAwareLazies)
		{
			l.uninit()
		}
	}

	abstract fun getView(): View?

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
			@Suppress("UNCHECKED_CAST")
			return _views.valueAt(index) as T?
		}
		else
		{
			val v = getView()?.findViewById(id)
			_views.put(id, v)
			@Suppress("UNCHECKED_CAST")
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
	fun <T : View> lazyView(id: Int): LazyView<T>
	{
		return LazyViewImpl(id)
	}

	/**
	 * Delegates that would rerun the initializer if the view is recreated
	 *
	 * @param initializer
	 * @return
	 */
	fun <T : Any?> viewAwareLazy(initializer: () -> T): ViewAwareLazy<T>
	{
		val inst = ViewAwareLazyImpl(initializer)
		_viewAwareLazies.add(inst)
		return inst
	}

	private inner class LazyViewImpl<T : View>(id: Int) : LazyView<T>
	{
		override operator fun getValue(thisRef: Any?, property: KProperty<*>): T
		{
			return findView(_id)
		}

		private val _id = id
	}

	private class ViewAwareLazyImpl<T : Any?>(initializer: () -> T)
			: ViewAwareLazy<T>
	{
		override operator fun getValue(thisRef: Any?, property: KProperty<*>): T
		{
			if (!_hasInitialized)
			{
				synchronized(_value)
				{
					obtainValue()
				}
			}
			// It's ok to just return because we are not clearing the array
			// during uninit, which make it safe even in a race condition
			return _value[0]
		}

		override fun uninit()
		{
			_hasInitialized = false
		}

		private fun obtainValue()
		{
			if (_value.isEmpty())
			{
				_value += _initializer()
			}
			else
			{
				_value[0] = _initializer()
			}
			_hasInitialized = true
		}

		private val _initializer = initializer
		private var _hasInitialized = false
		// we use an array because nullable var can't be lateinit
		private val _value = ArrayList<T>(1)
	}

	private val _views = SparseArray<View?>()
	private val _viewAwareLazies = arrayListOf<ViewAwareLazy<out Any?>>()
}
