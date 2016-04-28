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
public class ReceiveActivity extends Activity{
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
        String url = getString(R.string.url) + "customerRecivetJson.php";

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
                    map.put("name", c.getString("name"));
                    ArrListCustomer.add(map);
                }

                SimpleAdapter sAdap;
                sAdap = new SimpleAdapter(getBaseContext(), ArrListCustomer,
                        R.layout.activity_column_customer, new String[]{"name"}, new int[]{R.id.ColName});
                lvCustomer.setAdapter(sAdap);
                lvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView,
                                            int position, long mylng) {
                        String order_id = ArrListCustomer.get(position).get("order_id").toString();
                        String name = ArrListCustomer.get(position).get("name").toString();

                        Intent i = new Intent(getBaseContext(), CaptureSignature.class);
                        i.putExtra("order_id", order_id);
                        i.putExtra("name", name);
                        startActivityForResult(i, SIGNATURE_ACTIVITY);

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
