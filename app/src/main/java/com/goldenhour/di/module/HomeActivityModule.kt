package com.goldenhour.di.module

import com.goldenhour.ui.home.HomeActivity
import com.goldenhour.util.PermissionHelper
import com.goldenhour.util.location.LocationFetcher
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides

/**
 * Module for HomeActivity dependencies
 */
@Module
class HomeActivityModule {

    @Provides
    fun providesPermissionHelper(context: HomeActivity): PermissionHelper {
        return PermissionHelper(context)
    }

    @Provides
    fun provideFusedLocationClient(context: HomeActivity): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun provideLocationFetcher(context: HomeActivity): LocationFetcher {
        return LocationFetcher(context)
    }
}