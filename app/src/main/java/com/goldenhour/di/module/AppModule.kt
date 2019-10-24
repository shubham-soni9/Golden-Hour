package com.goldenhour.di.module

import android.content.Context
import androidx.room.Room
import com.goldenhour.BaseApplication
import com.goldenhour.common.AppConstants
import com.goldenhour.data.local.db.AppDatabase
import com.goldenhour.data.local.db.AppDbHelper
import com.goldenhour.data.local.db.DbHelper
import com.goldenhour.di.DatabaseInfo
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Module for application level dependencies
 */
@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    @Named("application_context")
    @Singleton
    fun provideContext(application: BaseApplication): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@DatabaseInfo dbName: String, @Named("application_context") context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, dbName)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @DatabaseInfo
    fun provideDatabaseName(): String {
        return AppConstants.DB_NAME
    }

    @Provides
    @Singleton
    fun provideDbHelper(mAppDataBase: AppDatabase): DbHelper {
        return AppDbHelper(mAppDataBase)
    }
}