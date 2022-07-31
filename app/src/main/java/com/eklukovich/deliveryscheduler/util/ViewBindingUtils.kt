package com.eklukovich.deliveryscheduler.util

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Delegate class for [ViewBinding] that automatically nulls out the Fragment's active view binding when [Fragment.onDestroyView] is called.
 * This is needed avoid memory leaks related to view binding and reduce the amount of boiler plate.
 *
 * Taken from: https://engineering.procore.com/fix-your-android-memory-leaks-in-fragments/
 */
fun <T : ViewBinding> Fragment.viewBinding(
    viewBindingFactory: (View) -> T
): ReadOnlyProperty<Fragment, T> = object : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    init {
        viewLifecycleOwnerLiveData.observe(this@viewBinding) { viewLifecycleOwner ->
            viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    binding = null
                }
            })
        }

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                view ?: error("You must either pass in the layout ID into ${this@viewBinding.javaClass.simpleName}'s constructor or inflate a view in onCreateView()")
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        binding?.let { return it }

        val viewLifecycleOwner = try {
            thisRef.viewLifecycleOwner
        } catch (e: IllegalStateException) {
            error("Should not attempt to get bindings when Fragment views haven't been created yet. The fragment has not called onCreateView() at this point.")
        }
        if (!viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            error("Should not attempt to get bindings when Fragment views are destroyed. The fragment has already called onDestroyView() at this point.")
        }

        return viewBindingFactory(thisRef.requireView()).also { viewBinding ->
            this.binding = viewBinding
        }
    }
}