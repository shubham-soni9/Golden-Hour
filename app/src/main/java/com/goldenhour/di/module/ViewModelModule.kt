package com.goldenhour.di.module

import androidx.lifecycle.ViewModel
import com.goldenhour.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeActivityViewModel(homeViewModel: HomeViewModel): ViewModel

}