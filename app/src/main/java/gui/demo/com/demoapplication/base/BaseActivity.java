package gui.demo.com.demoapplication.base;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import gui.demo.com.demoapplication.MainActivity;
import gui.demo.com.demoapplication.R;

/**
 * Created by 006283 on 2/11/2560.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    public static ProgressDialog dialog;
    private static double longitude;
    private static double latitude;

    private static android.location.LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.d(TAG, " Location change to : lat=" + latitude + " , lon=" + longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private static LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }

        if (locationManager == null) {
            checkApplicationPermission();
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        }

        if (isNetworkConnected() && isLocationEnabled()) {
            Toast.makeText(this, "Network and Location is available", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No network or location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    public Bundle getBundle() {
        Log.d(TAG, "getBundle");
        return getIntent().getExtras();
    }


    public void goToMainPage(View view) {
        Log.d(TAG, "goToMainPage click");
        Bundle bundle = getIntent().getExtras();
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtras(bundle);
        startActivityIfNeeded(i, 0);
        finish();
    }


    public void closeDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void showDialog(String label) {
        dialog.setMessage(label);
        dialog.show();
    }

    public void showLoadingDialog() {
        showDialog(getResources().getString(R.string.loading));
    }


    public Response.ErrorListener getDefaultErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                NetworkResponse response = error.networkResponse;
                String res = "";
                if (error instanceof ServerError && response != null) {
                    try {
                        res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                        Log.d(TAG, "ServerError : " + res);
                    } catch (Exception e1) {
                        Log.e(TAG, e1.getMessage(), e1);
                    }
                } else {
                    res = error.getMessage();
                }
                Log.e(TAG, "Error while calling REST API");
                Log.e(TAG, error.getMessage(), error);
                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                closeDialog();
            }

        };
    }


    public static double getLongitude() {
        return longitude;
    }

    public static double getLatitude() {
        return latitude;
    }


    public void checkApplicationPermission() {
        checkLocationPermission();
        checkNetworkPermission();
    }

    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
    }

    public void checkNetworkPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    0);
        }
    }


    public Boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }


    public Boolean isLocationEnabled() {
        LocationManager locationMgr = (LocationManager) getSystemService(Activity.LOCATION_SERVICE);
        if (locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }


}
