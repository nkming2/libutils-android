/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nkming.utils.transition

import android.annotation.TargetApi
import android.os.Build
import android.transition.Transition

/**
 * Utility adapter class to avoid having to override all three methods
 * whenever someone just wants to listen for a single event. It's the same class
 * from AOSP, but for some reason it's hidden there
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
open class TransitionListenerAdapter : Transition.TransitionListener
{
	override fun onTransitionStart(transition: Transition)
	{}

	override fun onTransitionEnd(transition: Transition)
	{}

	override fun onTransitionCancel(transition: Transition)
	{}

	override fun onTransitionPause(transition: Transition)
	{}

	override fun onTransitionResume(transition: Transition)
	{}
}
