package com.goldenhour.util.location;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.goldenhour.common.AppConstants;
import com.goldenhour.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Fetching location using Fused Location Provider API
 */
public class LocationFetcher implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final long CHECK_LOCATION_INTERVAL = 20000;
    private static final long LAST_LOCATION_TIME_THRESHOLD = 2 * 60000;

    private final String TAG = this.getClass().getSimpleName();

    private int priority;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationrequest;
    private Location location;

    private OnLocationChangedListener onLocationChangeListener;

    private Context context;


    private Handler checkLocationUpdateStartedHandler;
    private Runnable checkLocationUpdateStartedRunnable;

    public LocationFetcher(Context context) {
        this.onLocationChangeListener = (OnLocationChangedListener) context;
        this.context = context;
        this.priority = AppConstants.LocationPriority.BEST;
        connect();
    }

    public synchronized void connect() {

        destroy();

        int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        if (resp == ConnectionResult.SUCCESS) { // google play services working

            if (validatePreRequisites(context)) {   // location fetching enabled
                buildGoogleApiClient(context);
            }

        } else {                                // google play services not working
            Log.error("Google Play error", "=" + resp);
        }
        startCheckingLocationUpdates();
    }


    private void createLocationRequest() {
        locationrequest = new LocationRequest();
        locationrequest.setInterval(1);
        locationrequest.setFastestInterval(1);
        locationrequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private synchronized void buildGoogleApiClient(Context context) {

        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    private void startLocationUpdates() {

        createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationrequest, this);
    }

    @SuppressLint("LongLogTag")
    private Location getLocation() {

        try {
            if (location != null) {
                return location;
            } else {
                if (googleApiClient != null && googleApiClient.isConnected()) {
                    location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    Log.error("Fetching last fused location", "=" + location);
                    return location;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void destroy() {

        try {
            this.location = null;
            Log.error("location", "destroy");
            if (googleApiClient != null) {
                if (googleApiClient.isConnected()) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
                    googleApiClient.disconnect();
                } else if (googleApiClient.isConnecting()) {
                    googleApiClient.disconnect();
                }
            }
        } catch (Exception e) {
            Log.error("e", "=" + e.toString());
        }
        stopCheckingLocationUpdates();
    }


    private synchronized void startRequest() {

        try {
            startLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Log.error(TAG, "onConnected");
        Location loc = getLocation();
        if (loc != null) {
            onLocationChangeListener.onLocationChanged(loc, priority);
        }
        startRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {

        this.location = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        Log.error(TAG, "onConnectionFailed");
        this.location = null;
    }


    @Override
    public void onLocationChanged(Location location) {

        try {
            if (location != null) {

//                Log.i("-----------------------", "-----------------------");
//                Log.i("LOCATION_CHANGED", location.toString());
//                Log.i("-----------------------", "-----------------------");
                this.location = location;
                onLocationChangeListener.onLocationChanged(location, priority);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    private synchronized void startCheckingLocationUpdates() {
        checkLocationUpdateStartedHandler = new Handler();
        checkLocationUpdateStartedRunnable = () -> {
            if (LocationFetcher.this.location != null) {
                long timeSinceLastLocationFix = System.currentTimeMillis() - LocationFetcher.this.location.getTime();
                if (timeSinceLastLocationFix <= LAST_LOCATION_TIME_THRESHOLD) {
                    checkLocationUpdateStartedHandler.postDelayed(checkLocationUpdateStartedRunnable, CHECK_LOCATION_INTERVAL);
                }
            }
        };
        checkLocationUpdateStartedHandler.postDelayed(checkLocationUpdateStartedRunnable, CHECK_LOCATION_INTERVAL);
    }


    private synchronized void stopCheckingLocationUpdates() {
        try {
            if (checkLocationUpdateStartedHandler != null && checkLocationUpdateStartedRunnable != null) {
                checkLocationUpdateStartedHandler.removeCallbacks(checkLocationUpdateStartedRunnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            checkLocationUpdateStartedHandler = null;
            checkLocationUpdateStartedRunnable = null;
        }
    }

    public interface OnLocationChangedListener {

        /**
         * Override this method to listen to the Location Updates
         *
         * @param location
         * @param priority
         */
        void onLocationChanged(Location location, int priority);
    }

    public boolean validatePreRequisites(final Context activity) {

        boolean gpsEnabled = LocationUtils.INSTANCE.isGPSEnabled(activity);

        if (!gpsEnabled) {
            LocationAccess.showImproveAccuracyDialog(activity);
            return false;
        }
        return true;
    }
}
