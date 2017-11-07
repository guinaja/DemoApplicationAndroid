package gui.demo.com.demoapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 006283 on 2/11/2560.
 */

public class HttpUtils {

    private static final String TAG = "HttpUtils";

    private static HttpUtils httpUtils;

    private static RequestQueue requestQueue;

    private static Context context;

    private static ImageLoader imageLoader;

    public static synchronized HttpUtils getInstance(Context context) {

        if (httpUtils == null) {
            httpUtils = new HttpUtils();
            httpUtils.context = context;
            httpUtils.requestQueue = getRequestQueue(context);
        }

        if (imageLoader == null) {
            imageLoader = new ImageLoader(requestQueue,
                    new ImageLoader.ImageCache() {
                        private final LruCache<String, Bitmap>
                                cache = new LruCache<String, Bitmap>(20);

                        @Override
                        public Bitmap getBitmap(String url) {
                            return cache.get(url);
                        }

                        @Override
                        public void putBitmap(String url, Bitmap bitmap) {
                            cache.put(url, bitmap);
                        }
                    });
        }
        return httpUtils;
    }

    private static RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            // This setups up a new request queue which we will need to make HTTP requests.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext(), new ProxiedHurlStack());
        }
        return requestQueue;
    }

    private <T> void addToRequestQueue(Request<T> req) {
        this.requestQueue.add(req);
    }

    /**
     * class for proxy problem
     */
    static class ProxiedHurlStack extends HurlStack {
        private static final String PROXY_ADDRESS = "202.22.11.35";
        private static final int PROXY_PORT = 8080;//change with the port of the proxy

        @Override
        protected HttpURLConnection createConnection(URL url) throws IOException {
            Proxy proxy = new Proxy(Proxy.Type.HTTP,
                    InetSocketAddress.createUnresolved(PROXY_ADDRESS, PROXY_PORT));
            return (HttpURLConnection) url.openConnection(proxy);
        }
    }

    /*************************************************************/
    /**                 end of core method                      **/
    /*************************************************************/

    /**
     * method get rest api user jsonArray response
     *
     * @param url
     * @param responseListener
     * @param errorListener
     * @throws Exception
     */
    public void GETArray(String url, Response.Listener<JSONArray> responseListener
            , Response.ErrorListener errorListener) throws Exception {
        Log.d(TAG, "call GET URL : " + url);
        // This will instantiate an empty JSON request with the correct format.
        JSONArray reqBody = new JSONArray("{}");
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, reqBody, responseListener, errorListener) {
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        // Set the tag on the request.
        req.setTag(TAG);
        req.setShouldCache(Boolean.FALSE);
        addToRequestQueue(req);
    }

    /**
     * method get rest api user jsonObjecr response
     * @param url
     * @param responseListener
     * @param errorListener
     * @throws Exception
     */
    public void GETObject(String url, Response.Listener<JSONObject> responseListener
            , Response.ErrorListener errorListener) throws Exception {
        Log.d(TAG, "call GET URL : " + url);
        // This will instantiate an empty JSON request with the correct format.
        JSONObject reqBody = new JSONObject("{}");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, reqBody, responseListener, errorListener) {
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        // Set the tag on the request.
        req.setTag(TAG);
        req.setShouldCache(Boolean.FALSE);
        addToRequestQueue(req);
    }


}
