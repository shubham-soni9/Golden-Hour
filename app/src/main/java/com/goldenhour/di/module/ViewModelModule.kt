package com.goldenhour.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goldenhour.ViewModelFactory
import com.goldenhour.ui.home.HomeViewModel
import com.goldenhour.ui.home.dialog.SavedPinViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Module for injecting viewmodel with dagger multibindings
 *
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeActivityViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SavedPinViewModel::class)
    abstract fun bindSavedPinViewModel(savedPinViewModel: SavedPinViewModel): ViewModel

    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}