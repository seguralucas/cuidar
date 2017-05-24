package so.cuidar;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by GAS on 23/05/2017.
 */
public class GpsService extends Service implements LocationListener {


    public GpsService(LocationManager locationManager, Activity activity){
        this.locationManager=locationManager;
        this.activity=activity;
    }
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    // Declaring a Location Manager
    private LocationManager locationManager;
    private Activity activity;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 *  1;

    @Override
    public void onLocationChanged(Location newLocation) {
        this.location=newLocation;
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onCreate() {
        Log.i("myLogs", "onCreate");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i("myLogs", "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("myLogs", "onStartCommand");
        getLocation();
        if (location != null) {
            Log.i("myLogs", "lat = " + Double.toString(location.getLatitude()) + "lng = " + Double.toString(location.getLongitude()));
        } else
            Log.i("myLogs", "no location for your today");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("myLogs", "onDestroy");
        super.onDestroy();
    }

    public Location getLocation() {
        try {

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGPSEnabled) {
                stopSelf();
            } else {
                this.canGetLocation = true;
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (!(ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("myLogs", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }
}