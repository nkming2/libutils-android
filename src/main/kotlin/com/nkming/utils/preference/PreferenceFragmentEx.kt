package com.nkming.utils.preference

import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.View
import com.nkming.utils.app.FragmentViewAwareImpl

abstract class PreferenceFragmentEx : PreferenceFragment()
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
