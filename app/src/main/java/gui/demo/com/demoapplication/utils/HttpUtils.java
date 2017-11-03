package gui.demo.com.demoapplication.utils;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by 006283 on 2/11/2560.
 */

public class HttpUtils {

    private static final String TAG = "HttpUtils";

    private static HttpUtils httpUtils;

    private static RequestQueue requestQueue;

    private static AppCompatActivity callerActivity;

    public static HttpUtils getInstance(AppCompatActivity activity) {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
            callerActivity = activity;
            httpUtils.requestQueue = getRequestQueue(activity);
        }
        return httpUtils;
    }

    private static RequestQueue getRequestQueue(AppCompatActivity activity) {
        if (requestQueue == null) {
            // This setups up a new request queue which we will need to make HTTP requests.
            requestQueue = Volley.newRequestQueue(activity);
        }
        return requestQueue;
    }


    public void GETArray(String url, Response.Listener<JSONArray> responseListener
            , Response.ErrorListener errorListener) {
        Log.d(TAG, "call GET URL : " + url);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, responseListener, errorListener);
        this.requestQueue.add(req);
    }

    public void GETObject(String url, Response.Listener<JSONObject> responseListener
            , Response.ErrorListener errorListener) {
        Log.d(TAG, "call GET URL : " + url);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, responseListener, errorListener);
        this.requestQueue.add(req);
    }


}
