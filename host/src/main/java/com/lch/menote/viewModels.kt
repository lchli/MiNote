package com.lch.menote
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory

/**
 * Returns a [Lazy] delegate to access the ComponentActivity's ViewModel, if [factoryProducer]
 * is specified then [ViewModelProvider.Factory] returned by it will be used
 * to create [ViewModel] first time.
 *
 * ```
 * class MyComponentActivity : ComponentActivity() {
 *     val viewmodel: MyViewModel by viewmodels()
 * }
 * ```
 *
 * This property can be accessed only after the Activity is attached to the Application,
 * and access prior to that will result in IllegalArgumentException.
 */
@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModel(
    noinline factoryProducer: (() -> Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }

    return ViewModelLazy(VM::class, { viewModelStore }, factoryPromise)
}

