package com.nutcarcare.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
 * Created by Administrator on 3/19/2016.
 */
public class UpdateStatusActivity extends Activity{
    public static final int SIGNATURE_ACTIVITY = 1;
    private ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> tmpMyArrList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> ArrListCustomer = new ArrayList<HashMap<String, String>>();
    private ListView lvCustomer;
    private Http http = new Http();
    private HashMap<String, String> map;
    private Button btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle extras = getIntent().getExtras();
        // เช็คว่ามีค่าที่ส่งมาจากหน้าอื่นหรือไม่ถ้ามีจะไม่เท่ากับ null
        if (extras != null) {
            tmpMyArrList = (ArrayList<HashMap<String, String>>) extras
                    .getSerializable("MyArrList");
            if (tmpMyArrList != null) {
                MyArrList = tmpMyArrList;
            }
        }

        lvCustomer = (ListView)findViewById(R.id.listViewCustomer);
        btBack = (Button)findViewById(R.id.btnBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OrderCustomer();
                Intent i = new Intent(getBaseContext(), MenuActivity.class);
                i.putExtra("MyArrList", MyArrList);
                startActivity(i);
            }
        });

        LoadNameCustomer();
    }

    private void LoadNameCustomer() {
        String url = getString(R.string.url) + "customerStutustJson.php";

        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("no_parameter", ""));

        try {
            JSONArray data = new JSONArray(http.getJSONUrl(url, params));
            if (data.length() > 0) {
                ArrListCustomer.clear();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    map = new HashMap<String, String>();
                    map.put("order_id", c.getString("id"));
                    map.put("order_date", c.getString("order_date"));
                    map.put("status", c.getString("status"));
                    map.put("license_plate", c.getString("license_plate"));
                    map.put("name", c.getString("name"));
                    map.put("mobile", c.getString("mobile"));
                    map.put("email", c.getString("email"));
                    ArrListCustomer.add(map);
                }

                SimpleAdapter sAdap;
                sAdap = new SimpleAdapter(getBaseContext(), ArrListCustomer,
                        R.layout.activity_column_cust_car, new String[]{"name", "license_plate"}, new int[]{R.id.ColName, R.id.ColLicensePlate});
                lvCustomer.setAdapter(sAdap);
                lvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView,
                                            int position, long mylng) {
                        String order_id = ArrListCustomer.get(position).get("order_id").toString();
                        String order_date = ArrListCustomer.get(position).get("order_date").toString();
                        String status = ArrListCustomer.get(position).get("status").toString();
                        String license_plate = ArrListCustomer.get(position).get("license_plate").toString();
                        String name = ArrListCustomer.get(position).get("name").toString();
                        String mobile = ArrListCustomer.get(position).get("mobile").toString();
                        String email = ArrListCustomer.get(position).get("email").toString();

                        Intent i = new Intent(getBaseContext(), StatusDetailActivity.class);
                        i.putExtra("order_id", order_id);
                        i.putExtra("order_date", order_date);
                        i.putExtra("status", status);
                        i.putExtra("license_plate", license_plate);
                        i.putExtra("name", name);
                        i.putExtra("mobile", mobile);
                        i.putExtra("email", email);
                        startActivity(i);

                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode) {
            case SIGNATURE_ACTIVITY:
                if (resultCode == RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    String status  = bundle.getString("status");
                    if(status.equalsIgnoreCase("done")){
                    /*    Toast toast = Toast.makeText(this, "Signature capture successful!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 105, 50);
                        toast.show();*/
                    }
                }
                break;
        }

    }

}
