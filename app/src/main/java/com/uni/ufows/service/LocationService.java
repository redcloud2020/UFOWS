package com.uni.ufows.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.uni.ufows.R;
import com.uni.ufows.config.Parameters;
import com.uni.ufows.datalayer.models.GpsLog;
import com.uni.ufows.security.SecurePreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by sammy on 3/2/2017.
 */

public class LocationService extends Service
{
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    private int vehicleId;
    private int driverId;
    private String userId;

    Intent intent;
    int counter = 0;

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    public void onCreate()
    {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }



    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }



    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }




    public class MyLocationListener implements LocationListener
    {

        public void onLocationChanged(final Location loc)
        {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Log.i("********", "Location changed");
                    if(isBetterLocation(loc, previousBestLocation)) {
                        loc.getLatitude();
                        loc.getLongitude();
                        SecurePreferences.getInstance(getApplicationContext()).put(Parameters.LONGITUDE, loc.getLongitude()+"");
                        SecurePreferences.getInstance(getApplicationContext()).put(Parameters.LATITUDE, loc.getLatitude()+"");
                        intent.putExtra("Latitude", loc.getLatitude());
                        intent.putExtra("Longitude", loc.getLongitude());
                        intent.putExtra("Provider", loc.getProvider());
                        Log.e("lat", loc.getLatitude()+" "+loc.getLongitude());

                        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String format = s.format(new Date());

                        if(!TextUtils.isEmpty(SecurePreferences.getInstance(getApplicationContext()).getString(Parameters.DRIVER_NUMBER))) {
                            driverId = Integer.parseInt(SecurePreferences.getInstance(getApplicationContext()).getString(Parameters.DRIVER_NUMBER));
                        }
                        if(!TextUtils.isEmpty(SecurePreferences.getInstance(getApplicationContext()).getString(Parameters.USER_NUMBER))) {
                            userId = SecurePreferences.getInstance(getApplicationContext()).getString(Parameters.USER_ID_FOR_LOG);
                        }
                        if(!TextUtils.isEmpty(SecurePreferences.getInstance(getApplicationContext()).getString(Parameters.VEHICLE_NUMBER))) {
                            vehicleId = Integer.parseInt(SecurePreferences.getInstance(getApplicationContext()).getString(Parameters.VEHICLE_NUMBER));
                        }
                        try {
                            ActiveAndroid.beginTransaction();
                            GpsLog gpsLog = new GpsLog(UUID.randomUUID().toString(), format, "",
                                    "",
                                    userId,
                                    loc.getLatitude(), loc.getLongitude());
                            gpsLog.save();
                            ActiveAndroid.setTransactionSuccessful();

                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            ActiveAndroid.endTransaction();
                        }
                        sendBroadcast(intent);

                    }
                }
            }, 120000);

        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), getString(R.string.gps_disabled), Toast.LENGTH_SHORT ).show();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), getString(R.string.gps_enabled), Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }
}