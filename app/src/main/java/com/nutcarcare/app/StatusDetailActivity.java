package com.nutcarcare.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.http.Http;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 14/06/2559.
 */
public class StatusDetailActivity extends Activity{
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
    private String[] photoCarArray = new String[5];
    private String[] tmpPhotoCarArray;
    private String front_image = "", left_image = "", right_image = "", behide_image = "", top_image = "";
    private ArrayList sumService = new ArrayList<String>();
    private String order_id = "",  name = "", license_plate = "", phone = "", order_date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle extras = getIntent().getExtras();
        // เช็คว่ามีค่าที่ส่งมาจากหน้าอื่นหรือไม่ถ้ามีจะไม่เท่ากับ null
        if (extras != null) {
            order_id = extras.getString("order_id");
            order_date = extras.getString("order_date");
            license_plate = extras.getString("license_plate");
            name = extras.getString("name");
            phone = extras.getString("phone");
        }
    }


}
