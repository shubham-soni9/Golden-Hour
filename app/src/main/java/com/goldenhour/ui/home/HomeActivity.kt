package com.goldenhour.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.goldenhour.BR
import com.goldenhour.R
import com.goldenhour.common.AppConstants
import com.goldenhour.common.AppConstants.MAP_UPDATED_LOCATION_DIFFERENCE
import com.goldenhour.common.Codes
import com.goldenhour.data.model.db.PinnedLocation
import com.goldenhour.databinding.ActivityHomeBinding
import com.goldenhour.ui.base.BaseActivity
import com.goldenhour.ui.home.dialog.OnSavedLocationListener
import com.goldenhour.ui.home.dialog.SavedPinDialog
import com.goldenhour.util.*
import com.goldenhour.util.dialog.AlertDialog
import com.goldenhour.util.location.LocationAccess
import com.goldenhour.util.location.LocationFetcher
import com.goldenhour.util.map.MapStateListener
import com.goldenhour.util.map.MapUtils
import com.goldenhour.util.map.TouchableMapFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.widget.Autocomplete
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Home
 */
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(), OnMapReadyCallback,
    PermissionCallbacks, LocationFetcher.OnLocationChangedListener, OnSavedLocationListener {
    private val TAG = HomeActivity::class.java.simpleName
    private lateinit var mMap: GoogleMap

    @Inject
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var permissionHelper: PermissionHelper

    @Inject
    lateinit var locationFetcher: LocationFetcher

    override fun getLayoutRes(): Int {
        return R.layout.activity_home
    }

    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        mViewModel.allDataModel.observe(this, Observer {
            mViewDataBinding.txtDate.text = it.currentDate
            mViewDataBinding.tvMoonrise.text = it.moonrise
            mViewDataBinding.tvMoonset.text = it.moonset
            mViewDataBinding.tvSunrise.text = it.sunrise
            mViewDataBinding.tvSunset.text = it.sunset
            mViewDataBinding.tvSearch.text = it.address
        })
    }

    private fun init() {
        setSupportActionBar(toolbar)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googlemap: GoogleMap) {
        info(TAG, "onMapReady")
        mMap = googlemap
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.google_map) as TouchableMapFragment
        mMap.uiSettings.isZoomGesturesEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.uiSettings.isTiltGesturesEnabled = false
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.setOnMarkerClickListener { true }

        object : MapStateListener(mMap, mapFragment, this) {

            override fun onMapTouched() {
            }

            override fun onMapReleased() {

            }

            override fun onMapUnsettled() {
                mViewDataBinding.tvSearch.text = getString(R.string.getting_address)
            }

            override fun onMapSettled() {
                val newLatLng = mMap.cameraPosition.target
                val oldLatLng = mViewModel.getCurrentLatLng()
                val distanceCalculated = Utils.getDistance(oldLatLng, newLatLng)

                if (distanceCalculated > MAP_UPDATED_LOCATION_DIFFERENCE) {
                    if (NetworkUtils.isDeviceOnline(this@HomeActivity)) {
                        CoroutineScope(IO).launch {
                            getAddressFromLatLng(newLatLng)
                        }
                    } else {
                        mViewDataBinding.tvSearch.text = mViewModel.getCurrentAddress()
                        Toasty.error(
                            this@HomeActivity,
                            R.string.not_connected_to_internet_text,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    mViewDataBinding.tvSearch.text = mViewModel.getCurrentAddress()
                }
            }
        }
        checkPermissionAndSetLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Codes.REQ_LOCATION_PERMISSION -> {
                permissionHelper.onRequestPermissionResult(requestCode, permissions, grantResults)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun checkPermissionAndSetLocation() {
        if (NetworkUtils.isDeviceOnline(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationFetcher.connect()
            } else {
                permissionHelper.requestLocationPermissions(false)
            }
        } else {
            AlertDialog.Builder(this).message(R.string.internet_is_required).button(R.string.ok)
                .listener(object : AlertDialog.Listener {
                    override fun performPostAlertAction(purpose: Int, backpack: Bundle?) {
                        checkPermissionAndSetLocation()
                    }
                }).build().show()
        }
    }

    private suspend fun getAddressFromLatLng(latLng: LatLng) {
        val result = MapUtils.getGAPIAddress(this, latLng)
        withContext(Dispatchers.Main) {
            moveMap(latLng, result)
        }
    }

    override fun onPermissionGranted() {
        info(TAG, "Permission Granted")
        checkPermissionAndSetLocation()
    }

    override fun onPermissionRejected() {
        info(TAG, "Permission Rejected")
        AlertDialog.Builder(this).message(R.string.rationale_ask).button(R.string.ok)
            .listener(object : AlertDialog.Listener {
                override fun performPostAlertAction(purpose: Int, backpack: Bundle?) {
                    val permissions = arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    permissionHelper.requestPermissions(
                        permissions,
                        Codes.REQ_LOCATION_PERMISSION
                    )
                }
            }).build().show()
    }

    fun openPlaceSearch(view: View) {
        Utils.searchPlace(this, Codes.REQ_PLACE_SEARCH)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Codes.REQ_PLACE_SEARCH) {
            if (resultCode == RESULT_OK && data != null) {
                val place = Autocomplete.getPlaceFromIntent(data)
                place.latLng?.let { place.address?.let { it1 -> moveMap(it, it1) } }
            }
        } else if (requestCode == Codes.LOCATION_ACCESS_REQUEST) {
            if (resultCode == RESULT_OK) {
                checkPermissionAndSetLocation()
            } else {
                AlertDialog.Builder(this).message(R.string.rationale_ask).button(R.string.ok)
                    .listener(object : AlertDialog.Listener {
                        override fun performPostAlertAction(purpose: Int, backpack: Bundle?) {
                            LocationAccess.showImproveAccuracyDialog(this@HomeActivity)
                        }
                    }).build().show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun moveMap(latLng: LatLng, address: String) {
        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(15f).tilt(41.25f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mViewModel.updateByLocation(latLng, address)
    }

    fun saveGoldenHour(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            mViewModel.saveGoldenHour()
            withContext(Dispatchers.Main) {
                Toasty.success(this@HomeActivity, R.string.location_inserted, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun showBookmarkDialog(view: View) {
        SavedPinDialog.newInstance().show(supportFragmentManager)
    }

    override fun onLocationChanged(location: Location?, priority: Int) {
        if (location != null && location.latitude != 0.0 && location.longitude != 0.0) {
            CoroutineScope(IO).launch {
                getAddressFromLatLng(LatLng(location.latitude, location.longitude))
            }
            locationFetcher.destroy()
        }
    }

    override fun onSavedLocationClicked(pinnedLocation: PinnedLocation) {
        mViewModel.setCurrentGoldenHour(pinnedLocation)
        moveMap(pinnedLocation.location, pinnedLocation.address)
    }
}
