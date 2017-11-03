package gui.demo.com.demoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import gui.demo.com.demoapplication.base.BaseActivity;
import gui.demo.com.demoapplication.utils.HttpUtils;
import gui.demo.com.demoapplication.utils.TextValidateUtils;

public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";
    private String exampleAPI = "http://www.geognos.com/api/en/countries/info/all.jsons";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get object drop down
        HttpUtils httpUtils = HttpUtils.getInstance(this);
        Response.Listener<JSONObject> resposneListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    try {
                        String statusMsg = (String) response.get("StatusMsg");
                        Log.d(TAG, "statusMsg : " + statusMsg);

                        JSONObject results = response.getJSONObject("Results");
                        JSONArray countries = results.names();
                        // Loop through the array elements
                        for (int i = 0; i < countries.length(); i++) {
                            // Get current json object
                            String countryName = countries.getString(i);
                            Log.d(TAG, "country : " + countryName);
                            JSONObject country = results.getJSONObject(countryName);
                            Log.d(TAG, "country : " + country.toString());
                        }

                        Log.d(TAG, "jsonObj : " + response.toString());
                    } catch (Exception e) {
                        // If there is an error then output this to the logs.
                        Log.e(TAG, "Invalid JSON Object.");
                    }

                    closeDialog();
                } else {
                    Log.d(TAG, "No response found.");
                }
            }
        };
        showLoadingDialog();

        // call api
        httpUtils.GETObject(exampleAPI, resposneListener, getDefaultErrorListener());


        EditText textEmail = findViewById(R.id.text_email);
        textEmail.setVisibility(View.INVISIBLE);
        RadioGroup radioPageGroup = findViewById(R.id.radio_group_main);
        radioPageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "onCheckedChanged radioPageId : " + checkedId);
                EditText textEmail = findViewById(R.id.text_email);
                if (R.id.radio_page2 == checkedId) {
                    textEmail.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(), "require information", Toast.LENGTH_SHORT).show();
                } else {
                    textEmail.setVisibility(View.INVISIBLE);
                }

            }
        });
        Log.d(TAG, "onCreate successful");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "AppMainActivity onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "AppMainActivity onResume");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "AppMainActivity onPostResume");
        RadioGroup radioPageGroup = (RadioGroup) findViewById(R.id.radio_group_main);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String radioPage = bundle.getString("radioPage");

            int count = radioPageGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View o = radioPageGroup.getChildAt(i);
                if (o instanceof RadioButton) {
                    String radioPageTemp = ((RadioButton) o).getText().toString();
                    int radioPageId = ((RadioButton) o).getId();
                    Log.d(TAG, "radioPageGroup radioPageId : " + radioPageId);
                    if (radioPageTemp != null && radioPageTemp.equals(radioPage)) {
                        ((RadioButton) o).setChecked(Boolean.TRUE);
                    }
                }
            }
        }
        int selectedId = radioPageGroup.getCheckedRadioButtonId();
        RadioButton radioPageButton = (RadioButton) findViewById(selectedId);
        Log.d(TAG, "radioPage.id  : " + radioPageButton.getText());
    }

    public void nextPage(View view) {
        Log.d(TAG, "nextPageBtn click");

        RadioGroup radioPageGroup = (RadioGroup) findViewById(R.id.radio_group_main);
        int selectedId = radioPageGroup.getCheckedRadioButtonId();

        RadioButton radioPageButton = (RadioButton) findViewById(selectedId);
        Log.d(TAG, "radioPage.id  : " + radioPageButton.getText());
        Bundle bundle = new Bundle();
        bundle.putString("radioPage", radioPageButton.getText().toString());


        Log.d(TAG, "radioPageGroup selectedId : " + selectedId);
        Class pageClass;
        if (R.id.radio_page1 == selectedId) {
            pageClass = Page1Activity.class;
        } else {
            pageClass = Page2Activity.class;
            EditText textEmail = findViewById(R.id.text_email);
            if (textEmail.getText() == null || "".equals(textEmail.getText().toString())) {
                String errorMsg = "e-mail is required";
                textEmail.setError(errorMsg);
                return;
            } else {
                if (!TextValidateUtils.isValidEmail(textEmail.getText().toString())) {
                    String errorMsg = "invalid e-mail pattern";
                    textEmail.setError(errorMsg);
                    return;
                }
                bundle.putString("text-email", textEmail.getText().toString());
            }
        }


        Intent i = new Intent(this, pageClass);
        i.putExtras(bundle);
        startActivityIfNeeded(i, 0);
    }


}
