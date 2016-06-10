package com.nkming.utils.app

import android.content.Context
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.transition.TransitionInflater
import android.view.View
import com.nkming.utils.R

object FragmentTransitionHelper
{
	/**
	 *  @param isAllowingStateLoss See FragmentTransaction#commitAllowingStateLoss()
	 */
	fun startTransitionAnimation(context: Context, src: Fragment,
			srcActivityContainerId: Int, target: Fragment, shared: View,
			sharedName: String, isAllowingStateLoss: Boolean = false)
	{
		var transition = buildAnimationTransaction(context, src,
				srcActivityContainerId, target, shared, sharedName)
		if (!isAllowingStateLoss)
		{
			transition.commit()
		}
		else
		{
			transition.commitAllowingStateLoss()
		}
	}

	/**
	 *  @param isAllowingStateLoss See FragmentTransaction#commitAllowingStateLoss()
	 */
	fun startTransitionAnimation(context: Context, src: Fragment,
			srcActivityContainerId: Int, target: Fragment,
			isAllowingStateLoss: Boolean = false)
	{
		var transition = buildAnimationTransaction(context, src,
				srcActivityContainerId, target)
		if (!isAllowingStateLoss)
		{
			transition.commit()
		}
		else
		{
			transition.commitAllowingStateLoss()
		}
	}

	private fun buildAnimationTransaction(context: Context, src: Fragment,
			srcActivityContainerId: Int, target: Fragment, shared: View,
			sharedName: String): FragmentTransaction
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			val inflater = TransitionInflater.from(context)
			src.sharedElementReturnTransition = inflater.inflateTransition(
					R.transition.change_image_transition)
			src.exitTransition = inflater.inflateTransition(
					android.R.transition.fade)

			target.sharedElementEnterTransition = inflater.inflateTransition(
					R.transition.change_image_transition)
			target.enterTransition = inflater.inflateTransition(
					android.R.transition.fade)

			return src.fragmentManager.beginTransaction()
					.addSharedElement(shared, sharedName)
					.replace(srcActivityContainerId, target)
		}
		else
		{
			return src.fragmentManager.beginTransaction()
					.setCustomAnimations(android.R.anim.fade_in,
							android.R.anim.fade_out)
					.replace(srcActivityContainerId, target)
		}
	}

	private fun buildAnimationTransaction(context: Context, src: Fragment,
			srcActivityContainerId: Int, target: Fragment): FragmentTransaction
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			val inflater = TransitionInflater.from(context)
			src.exitTransition = inflater.inflateTransition(
					android.R.transition.fade)
			target.enterTransition = inflater.inflateTransition(
					android.R.transition.fade)

			return src.fragmentManager.beginTransaction()
					.replace(srcActivityContainerId, target)
		}
		else
		{
			return src.fragmentManager.beginTransaction()
					.setCustomAnimations(android.R.anim.fade_in,
							android.R.anim.fade_out)
					.replace(srcActivityContainerId, target)
		}
	}
}
