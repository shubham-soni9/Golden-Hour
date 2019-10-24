package com.goldenhour.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * Base structure for defining Activity
 */
abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity(),
    HasAndroidInjector {
    lateinit var mViewDataBinding: T
    lateinit var mViewModel: V

    // Injecting activity
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    // Injecting viewmodel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * @return layout resource id
     */
    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    protected abstract fun getViewModel(): Class<V>

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    /**
     * Connecting view with data binding and binding viewmodel with view
     *
     */
    private fun performDataBinding() {
        mViewModel = ViewModelProvider(this, viewModelFactory).get(getViewModel())
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutRes())
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding.executePendingBindings()
    }

    /**
     *  Performs members-injection for a concrete subtype of a activity
     */
    override fun androidInjector(): AndroidInjector<Any>? {
        return androidInjector
    }
}