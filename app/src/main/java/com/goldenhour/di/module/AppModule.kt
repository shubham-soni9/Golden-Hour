package com.goldenhour.di.module

import android.content.Context
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideContext(context: Context): Context

}