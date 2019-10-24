package com.goldenhour.di.builder

import com.goldenhour.ui.home.dialog.SavedPinDialog

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Providing support for injecting module with android components
 */
@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    internal abstract fun contributeSavedPinDialogFragment(): SavedPinDialog

}
