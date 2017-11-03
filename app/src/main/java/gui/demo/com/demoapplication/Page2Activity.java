package gui.demo.com.demoapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import gui.demo.com.demoapplication.base.BaseActivity;

public class Page2Activity extends BaseActivity {

    public static final String TAG = "Page2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);


        Bundle bundle = getIntent().getExtras();
        String radioPage = bundle.getString("radioPage");
        Toast.makeText(this, radioPage, Toast.LENGTH_SHORT).show();

        String textEmail = bundle.getString("text-email");
        TextView pageResult = findViewById(R.id.text_email);
        pageResult.setText("hello " + textEmail);

        Log.d(TAG, "onCreate successful");
    }
}
