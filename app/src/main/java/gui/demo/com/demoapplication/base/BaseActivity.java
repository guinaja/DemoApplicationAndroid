package gui.demo.com.demoapplication.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
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
}
