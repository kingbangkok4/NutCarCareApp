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
public class CarActivity extends Activity {
    // private DatabaseActivity myDb = new DatabaseActivity(this);
    ArrayList<HashMap<String, String>> CarList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map;
    private Spinner spinner_type_car;
    private Button btBack, btPhotoCare, btMain;
    private EditText txtCustomer, txtMobile, txtEmail, txtLicensePlate, txtBrand, txtColor, txtScar;
    private String[] type_care;
    private Http http = new Http();
    private ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> CarTypeArrList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> tmpMyArrList = new ArrayList<HashMap<String, String>>();
    private Double sumTotal = 0.00;
    private String strService = "", type = "", license_plate = "", brand = "", color = "", scar = "", cust_id = "";
    private String[] photoCarArray = new String[5];
    private String[] tmpPhotoCarArray;
    private String front_image = "", left_image = "", right_image = "", behide_image = "", top_image = "";
    private ArrayList sumService = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        Bundle extras = getIntent().getExtras();
        // เช็คว่ามีค่าที่ส่งมาจากหน้าอื่นหรือไม่ถ้ามีจะไม่เท่ากับ null
        if (extras != null) {
            tmpMyArrList = (ArrayList<HashMap<String, String>>) extras
                    .getSerializable("MyArrList");
            if (tmpMyArrList != null) {
                MyArrList = tmpMyArrList;
            }
            tmpPhotoCarArray = (String[]) extras
                    .getSerializable("photoCarArray");
            if (tmpPhotoCarArray != null) {
                photoCarArray = tmpPhotoCarArray;

                front_image = photoCarArray[0];
                left_image = photoCarArray[1];
                right_image = photoCarArray[2];
                behide_image = photoCarArray[3];
                top_image = photoCarArray[4];
            }

            license_plate = extras.getString("license_plate");
            brand = extras.getString("brand");
            color = extras.getString("color");
            scar = extras.getString("scar");
            type = extras.getString("type");
            cust_id = extras.getString("cust_id");
            sumTotal = extras.getDouble("sumTotal");
            strService = extras.getString("strService");
            sumService = (ArrayList<String>) extras
                    .getSerializable("sumService");
        }
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        spinner_type_car = (Spinner) findViewById(R.id.cmbType);
        btBack = (Button) findViewById(R.id.btnBack);
        btPhotoCare = (Button) findViewById(R.id.btnPhotoCare);
        btMain = (Button) findViewById(R.id.btnMain);


        txtLicensePlate = (EditText) findViewById(R.id.editTextLicensePlate);
        txtBrand = (EditText) findViewById(R.id.editTextBrand);
        txtColor = (EditText) findViewById(R.id.editTextColor);
        txtScar = (EditText) findViewById(R.id.editTextScar);


        txtLicensePlate.setText(license_plate);
        txtBrand.setText(brand);
        txtColor.setText(color);
        txtScar.setText(scar);

        LoadTypeCar();

        spinner_type_car.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                spinner_type_car.setSelection(position);
                type = (String) spinner_type_car.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), CustomerActivity.class);
                i.putExtra("MyArrList", MyArrList);
                i.putExtra("sumTotal", sumTotal);
                i.putExtra("strService", strService);
                startActivity(i);
            }
        });
        btMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), MenuActivity.class);
                i.putExtra("MyArrList", MyArrList);
                startActivity(i);
            }
        });
        btPhotoCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                license_plate = txtLicensePlate.getText().toString().trim();
                brand = txtBrand.getText().toString().trim();
                color = txtColor.getText().toString().trim();
                scar = txtLicensePlate.getText().toString().trim();
                if ("".equals(license_plate)
                        || "".equals(brand)
                        || "".equals(type)
                        || "".equals(color)
                        || "".equals(scar)
                        || "".equals(cust_id)) {
                    builder.setTitle("แจ้งเตือน");
                    builder.setMessage("กรุณาใส่ข้อมูลรถให้ครบถ้วน !")
                            .setCancelable(true)
                            .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("ปิด",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).show();
                } else {
                    Intent i = new Intent(getBaseContext(), PhotoCarActivity.class);
                    i.putExtra("MyArrList", MyArrList);

                    i.putExtra("license_plate", license_plate);
                    i.putExtra("brand", brand);
                    i.putExtra("color", color);
                    i.putExtra("scar", scar);
                    i.putExtra("type", type);
                    i.putExtra("cust_id", cust_id);

                    i.putExtra("sumTotal", sumTotal);
                    i.putExtra("strService", strService);
                    i.putExtra("sumService", sumService);
                    startActivity(i);
                }
            }
        });

    }


  /*  private void OrderConfirm() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String msgStatus = "";
        license_plate = txtLicensePlate.getText().toString().trim();
        brand = txtBrand.getText().toString().trim();
        color = txtColor.getText().toString().trim();
        scar = txtScar.getText().toString().trim();
        if ("".equals(license_plate)
                || "".equals(brand)
                || "".equals(type)
                || "".equals(color)
                || "".equals(scar)
                || "".equals(cust_id)) {
            builder.setTitle("แจ้งเตือน");
            builder.setMessage("กรุณาใส่ข้อมูลรถให้ครบถ้วน !")
                    .setCancelable(true)
                    .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("ปิด",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }).show();
        } else {
            String url = getString(R.string.url) + "saveOrder.php";
            // Paste Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("order_detail", strService));
            params.add(new BasicNameValuePair("price", sumTotal.toString()));
            params.add(new BasicNameValuePair("order_by", MyArrList.get(0).get("username")));
            params.add(new BasicNameValuePair("cust_id", cust_id));

            params.add(new BasicNameValuePair("license_plate", license_plate));
            params.add(new BasicNameValuePair("brand", brand));
            params.add(new BasicNameValuePair("type", type));
            params.add(new BasicNameValuePair("color", color));
            params.add(new BasicNameValuePair("scar", scar));

            params.add(new BasicNameValuePair("front_image", front_image));
            params.add(new BasicNameValuePair("left_image", left_image));
            params.add(new BasicNameValuePair("right_image", right_image));
            params.add(new BasicNameValuePair("behide_image", behide_image));
            params.add(new BasicNameValuePair("top_image", top_image));

            try {
                JSONArray data = new JSONArray(http.getJSONUrl(url, params));
                if (data.length() > 0) {
                    JSONObject c = data.getJSONObject(0);
                    msgStatus = c.getString("error");

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                msgStatus = e.getMessage();
            }
            builder.setTitle("สถานะการบันทึก");
            final String finalMsgStatus = msgStatus;
            builder.setMessage(msgStatus)
                    .setCancelable(true)
                    .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if ("บันทึกการใช้บริการสำเร็จ".equals(finalMsgStatus)){
                                Intent i = new Intent(getBaseContext(), MenuActivity.class);
                                i.putExtra("MyArrList", MyArrList);
                                startActivity(i);
                        }
                            dialog.cancel();
                        }
                    })*//*
                    .setNegativeButton("ปิด",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })*//*.show();
        }
    }*/

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
                    ArrListCustomer.add(map);
                }

                SimpleAdapter sAdap;
                sAdap = new SimpleAdapter(CarActivity.this, ArrListCustomer,
                        R.layout.activity_column_customer, new String[]{"name"}, new int[]{R.id.ColName});
                lvCustomer.setAdapter(sAdap);
                lvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView,
                                            int position, long mylng) {
                        String name = ArrListCustomer.get(position).get("name").toString();
                        txtName.setText("คุณ " + name);
                        builder.setCancelable(true);
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void LoadTypeCar() {
        String url = getString(R.string.url) + "carTypeListJson.php";
        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            JSONArray data = new JSONArray(http.getJSONUrl(url, params));
            if (data.length() > 0) {
                CarTypeArrList.clear();
                type_care = new String[data.length()];
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    map = new HashMap<String, String>();
                    map.put("id", c.getString("id"));
                    map.put("name", c.getString("name"));
                    CarTypeArrList.add(map);
                    type_care[i] = c.getString("name");
                }

                ArrayAdapter<String> dataAdapterType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type_care);
                dataAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_type_car.setAdapter(dataAdapterType);
                String type_name = CarTypeArrList.get(0).get("name");
                int innerPosition = dataAdapterType.getPosition(type_name);
                if (!"".equals(type)) {
                    innerPosition = dataAdapterType.getPosition(type);
                }
                spinner_type_car.setSelection(innerPosition);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}