package com.goldenhour.di.builder

import com.goldenhour.di.module.HomeActivityModule
import com.goldenhour.ui.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Providing support for injecting module with android components
 */
@Module(includes = [FragmentBuilderModule::class])
internal abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [HomeActivityModule::class])
    abstract fun homeActivity(): HomeActivity

}
