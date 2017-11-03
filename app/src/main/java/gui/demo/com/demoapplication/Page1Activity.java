package gui.demo.com.demoapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import gui.demo.com.demoapplication.base.BaseActivity;

public class Page1Activity extends BaseActivity {

    public static final String TAG = "Page1Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        Bundle bundle = getBundle();
        String radioPage = bundle.getString("radioPage");
        Toast.makeText(this, radioPage, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate successful");
    }
}
