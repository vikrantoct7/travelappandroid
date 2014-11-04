package saavy.brothers.LetsGoo;

import java.util.List;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.ContextThemeWrapper;
public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;
    
    
    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } 
            else 
            {
                if (isNetworkEnabled) 
                {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    //Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            this.canGetLocation = true;
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) 
                {
                    if (location == null) 
                    {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        //Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) 
                        {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) 
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                this.canGetLocation = true;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }       
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will launch Settings Options
     * */
    public void showSettingsAlert(){
    	ContextThemeWrapper wrapper=new ContextThemeWrapper(mContext,R.style.MyAlertDialogStyle);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(wrapper);
        
        // Setting Dialog Title
        alertDialogBuilder.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialogBuilder.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialogBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
        
        // Showing Alert Message
        alertDialogBuilder.show();
        //Dialog alertDialog= alertDialogBuilder.create();
       // alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.null_image);
       // alertDialog.show();

    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getCurrentLocation(Context baseContext) 
	{

		StringBuilder strLocation = new StringBuilder();
        List<Address> addresses;
        try {
            addresses = Geocoder.getFromLocation(getLatitude(),	getLongitude()	, 1);
            if (addresses.size() > 0)
            {
            	Address add = addresses.get(0);
            	
            	if(add.getSubLocality() !=null)
            	{
            		strLocation.append(add.getSubLocality());
            	}
            	if(strLocation.length()==0 && add.getAddressLine(0) !=null)
            	{
            		strLocation.append(add.getAddressLine(0));
            	}
            	if(strLocation.length()==0 && add.getLocality() !=null)
            	{
            		strLocation.append(add.getLocality());
            	}
            }
        } 
        catch (Exception e) {
            //e.printStackTrace();
        }
        return strLocation.toString();
	}
	
	public String getCurrentCity()
	{
		String userCity="";
        List<Address> addresses;
        try {
            addresses = Geocoder.getFromLocation(getLatitude(),	getLongitude()	, 1);
            if (addresses.size() > 0)
            {
            	Address add = addresses.get(0);
            	userCity =add.getLocality();
            	
            }
        } 
        catch (Exception e) {
            //e.printStackTrace();
        }
		return userCity;
	}
}