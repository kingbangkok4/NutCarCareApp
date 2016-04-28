package com.nutcarcare.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
   // private DatabaseActivity myDb = new DatabaseActivity(this);
    HashMap<String, String> map;
    private Button btSearch, btCare, btBack, btMain;
    private EditText txtCustomer, txtMobile, txtEmail;
    private String cust_id = "0", name, mobile, email;
    private Http http = new Http();

    private ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> tmpMyArrList = new ArrayList<HashMap<String, String>>();
    private Double sumTotal = 0.00;
    private String strService = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        Bundle extras = getIntent().getExtras();
        // เช็คว่ามีค่าที่ส่งมาจากหน้าอื่นหรือไม่ถ้ามีจะไม่เท่ากับ null
        if (extras != null) {
            tmpMyArrList = (ArrayList<HashMap<String, String>>) extras
                    .getSerializable("MyArrList");
            if (tmpMyArrList != null) {
                MyArrList = tmpMyArrList;
            }
            sumTotal = extras.getDouble("sumTotal");
            strService = extras.getString("strService");
        }

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btSearch = (Button) findViewById(R.id.btnSearch);
        btCare = (Button) findViewById(R.id.btnCare);
        btBack = (Button) findViewById(R.id.btnBack);
        btMain = (Button) findViewById(R.id.btnMain);

        txtCustomer = (EditText) findViewById(R.id.editTextCustomer);
        txtMobile = (EditText) findViewById(R.id.editTextMobile);
        txtEmail = (EditText) findViewById(R.id.editTextEmail);


        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSearchCustomer(txtCustomer.getText().toString().trim());
            }
        });
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        btCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgStatus = "";

                name = txtCustomer.getText().toString().trim();
                mobile = txtMobile.getText().toString().trim();
                email = txtEmail.getText().toString().trim();

                if ("".equals(name)
                        || "".equals(mobile)
                        || "".equals(email)){
                    builder.setTitle("แจ้งเตือน");
                    builder.setMessage("กรุณาใส่ข้อมูลลูกค้าให้ครบถ้วน !")
                            .setCancelable(true)
                            .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    /*.setNegativeButton("ปิด",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })*/.show();
                } else {
                    if(CheckCustomer()) {
                        Intent i = new Intent(getBaseContext(), CarActivity.class);
                        i.putExtra("MyArrList", MyArrList);
                        i.putExtra("sumTotal", sumTotal);
                        i.putExtra("strService", strService);
                        i.putExtra("cust_id", cust_id);
                        startActivity(i);
                    }
                }
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OrderCustomer();
                Intent i = new Intent(getBaseContext(), ServiceActivity.class);
                i.putExtra("MyArrList", MyArrList);
                startActivity(i);
            }
        });

        btMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), MenuActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean CheckCustomer() {
        boolean status = false;
        String url = getString(R.string.url) + "checkCustomer.php";
        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", txtCustomer.getText().toString().trim()));
        params.add(new BasicNameValuePair("mobile", txtMobile.getText().toString().trim()));
        params.add(new BasicNameValuePair("email", txtEmail.getText().toString().trim()));
        try {
            JSONArray data = new JSONArray(http.getJSONUrl(url, params));
            if (data.length() > 0) {
                JSONObject c = data.getJSONObject(0);
                cust_id = c.getString("cust_id");
                status = true;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return status;
    }

    private void DialogSearchCustomer(String strCustomer) {
        View listCustomerView = View.inflate(this, R.layout.dialog_list_customer, null);
        final ArrayList<HashMap<String, String>> ArrListCustomer = new ArrayList<HashMap<String, String>>();
        ListView lvCustomer = (ListView) listCustomerView.findViewById(R.id.listViewCustomer);
        final EditText txtName = (EditText) listCustomerView.findViewById(R.id.editTextName);

        String url = getString(R.string.url) + "customerListJson.php";

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("")
                .setView(listCustomerView)
                .setCancelable(true)
                .setPositiveButton("เลือก", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        txtCustomer.setText(name);
                        txtMobile.setText(mobile);
                        txtEmail.setText(email);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("ปิด",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();

        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", strCustomer));
        try {
            JSONArray data = new JSONArray(http.getJSONUrl(url, params));
            if (data.length() > 0) {
                ArrListCustomer.clear();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    map = new HashMap<String, String>();
                    map.put("id", c.getString("id"));
                    map.put("name", c.getString("name"));
                    map.put("mobile", c.getString("mobile"));
                    map.put("email", c.getString("email"));
                    ArrListCustomer.add(map);
                }

                SimpleAdapter sAdap;
                sAdap = new SimpleAdapter(getBaseContext(), ArrListCustomer,
                        R.layout.activity_column_customer, new String[]{"name"}, new int[]{R.id.ColName});
                lvCustomer.setAdapter(sAdap);
                lvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView,
                                            int position, long mylng) {
                        cust_id = ArrListCustomer.get(position).get("id").toString();
                        name = ArrListCustomer.get(position).get("name").toString();
                        mobile = ArrListCustomer.get(position).get("mobile").toString();
                        email = ArrListCustomer.get(position).get("email").toString();
                        txtName.setText(name);

                        builder.setCancelable(true);
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}