package gui.demo.com.demoapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import gui.demo.com.demoapplication.base.BaseActivity;
import gui.demo.com.demoapplication.utils.HttpUtils;

public class Page1Activity extends BaseActivity {

    public static final String TAG = "Page1Activity";

    private String exampleAPI = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";

    private Boolean startTracking = Boolean.FALSE;

    private HttpUtils httpUtils;
    private Response.Listener<JSONObject> responseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        Bundle bundle = getBundle();
        String radioPage = bundle.getString("radioPage");
        Toast.makeText(this, radioPage, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate successful");
    }

    @Override
    public void onLocationChange(double latitude, double longitude) {
        try {
            if (startTracking) {
                getGpsLocation().setText("LAT :  " + latitude + "\nLON : " + longitude);

                if (httpUtils == null) {
                    httpUtils = HttpUtils.getInstance(this.getApplicationContext());
                }
                StringBuilder urlEndpoint = new StringBuilder();
                urlEndpoint.append(exampleAPI);
                urlEndpoint.append(getLatitude());
                urlEndpoint.append(",");
                urlEndpoint.append(getLongitude());
                // call api
                showLoadingDialog();
                httpUtils.GETObject(urlEndpoint.toString(), getResponseGoogleListener(), getDefaultErrorListener());
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error during make request");
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "onLocationChange successful");
    }

    private Response.Listener<JSONObject> getResponseGoogleListener() {
        if (responseListener == null) {
            responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response.length() > 0) {
                        try {
                            Log.d(TAG, "response : " + response);
                            JSONArray results = response.getJSONArray("results");
                            String status = (String) response.get("status");
                            JSONObject data = results.getJSONObject(3);

                            String address = data.getString("formatted_address");
                            Log.d(TAG, "address : " + address);

                            String message = "Status : " + status + "\nAddress : " + address;
                            getResultLocation().setText(message);
                        } catch (Exception e) {
                            // If there is an error then output this to the logs.
                            Log.e(TAG, "Invalid JSON Object.");
                            Toast.makeText(Page1Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //closeDialog();
                    } else {
                        Log.d(TAG, "No response found.");
                    }
                }
            };
        }
        return responseListener;
    }

    private TextView getResultLocation() {
        return findViewById(R.id.text_result_location);
    }

    private TextView getGpsLocation() {
        return findViewById(R.id.text_gps_location);
    }

    public void testCallHttp(View view) {
        startTracking = !startTracking;
        if (!startTracking) {
            getGpsLocation().setText("");
            getResultLocation().setText("");
        }
    }
}
