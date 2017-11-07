package gui.demo.com.demoapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONObject;

import gui.demo.com.demoapplication.base.BaseActivity;
import gui.demo.com.demoapplication.utils.HttpUtils;

public class Page1Activity extends BaseActivity {

    public static final String TAG = "Page1Activity";

    private String exampleAPI = "http://samples.openweathermap.org/data/2.5/weather?";
    private String keyAPI = "&appid=b1b15e88fa797225412429c1c50c122a1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        Bundle bundle = getBundle();
        String radioPage = bundle.getString("radioPage");
        Toast.makeText(this, radioPage, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate successful");
    }

    public void testCallHttp(View view) {

        try {
            // get object drop down
            HttpUtils httpUtils = HttpUtils.getInstance(this.getApplicationContext());

            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    if (response.length() > 0) {
                        try {
                            JSONObject country = response.getJSONObject("sys");
                            String countryName = (String) country.get("country");


                            JSONObject results = response.getJSONObject("main");
                            String temp = results.getString("temp");
                            Log.d(TAG, "country : " + countryName);
                            Log.d(TAG, "temp : " + temp);
                            String message = "country : " + countryName + " temp : " + temp;
                            Log.d(TAG, "jsonObj : " + response.toString());

                            Toast.makeText(Page1Activity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // If there is an error then output this to the logs.
                            Log.e(TAG, "Invalid JSON Object.");
                            Toast.makeText(Page1Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        closeDialog();
                    } else {
                        Log.d(TAG, "No response found.");
                    }
                }
            };
            showLoadingDialog();

            StringBuilder urlEndpoint = new StringBuilder();
            urlEndpoint.append(exampleAPI);
            urlEndpoint.append("lat=");
            urlEndpoint.append(getLatitude());
            urlEndpoint.append("&lon=");
            urlEndpoint.append(getLongitude());
            urlEndpoint.append(keyAPI);
//            urlEndpoint.setLength(0);
//            urlEndpoint.append("https://api.github.com/users/hadley/orgs");
//            urlEndpoint.append("http://hmkcode.appspot.com/rest/controller/get.json");

            // call api
            httpUtils.GETObject(urlEndpoint.toString(), responseListener, getDefaultErrorListener());

        } catch (Exception ex) {
            Log.e(TAG, "Error during make request");
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
