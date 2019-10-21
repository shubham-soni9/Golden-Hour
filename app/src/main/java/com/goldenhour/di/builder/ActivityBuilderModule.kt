package com.goldenhour.di.builder

import com.goldenhour.ui.home.HomeActivity
import com.goldenhour.ui.home.HomeActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [HomeActivityModule::class])
    abstract fun contributeMainActivity(): HomeActivity

}
