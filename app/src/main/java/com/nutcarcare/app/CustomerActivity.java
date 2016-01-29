package com.nutcarcare.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.database.DatabaseActivity;
import com.http.Http;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 1/20/2016.
 */
public class CustomerActivity extends Activity {
    private DatabaseActivity myDb = new DatabaseActivity(this);
    ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map;
    private Spinner spinner_type_car;
    private String[] type_care;
    private String strType = "";
    private Http http = new Http();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        spinner_type_car = (Spinner) findViewById(R.id.cmbType);
        LoadTypeCar();
       /* spinner_type_car.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_type_car.setSelection(i);
                strType = (String) spinner_type_car.getSelectedItem();
            }
        });*/
    }

    private void LoadTypeCar() {
        String url = getString(R.string.url_localhost) + "carTypeList.php";
        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONArray data = new JSONArray(http.getJSONUrl(url, params));
            if (data.length() > 0) {
                MyArrList.clear();
                type_care = new String[data.length()];
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    map = new HashMap<String, String>();
                    map.put("id", c.getString("id"));
                    map.put("name", c.getString("name"));
                    MyArrList.add(map);
                    type_care[i] = c.getString("name");
                }

                ArrayAdapter<String> dataAdapterSex = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, type_care);
                dataAdapterSex
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_type_car.setAdapter(dataAdapterSex);

                String type = MyArrList.get(0).get("name");
                int spinnerPositionSex = dataAdapterSex.getPosition(type);
                spinner_type_car.setSelection(spinnerPositionSex);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}