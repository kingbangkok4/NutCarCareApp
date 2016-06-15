package com.nutcarcare.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.common.CommonClass;
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
 * Created by Administrator on 14/06/2559.
 */
public class StatusDetailActivity extends Activity {
    // private DatabaseActivity myDb = new DatabaseActivity(this);
    ArrayList<HashMap<String, String>> CarList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map;
    private Spinner spinner_type_car;
    private Button btnBack, btnCompleted, btnCancel;
    private EditText txtName, txtMobile, txtLicensePlate, txtDate, txtStatus;
    private String[] type_care;
    private Http http = new Http();
    private CommonClass common = new CommonClass();
    private ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> CarTypeArrList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> tmpMyArrList = new ArrayList<HashMap<String, String>>();
    private Double sumTotal = 0.00;
    private String[] photoCarArray = new String[5];
    private String[] tmpPhotoCarArray;
    private String front_image = "", left_image = "", right_image = "", behide_image = "", top_image = "";
    private ArrayList sumService = new ArrayList<String>();
    private String order_id = "", name = "", license_plate = "", mobile = "", order_date = "", status = "", email = "";

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
            status = extras.getString("status");
            license_plate = extras.getString("license_plate");
            name = extras.getString("name");
            mobile = extras.getString("mobile");
            email = extras.getString("email");
        }
        //txtName, txtMobile, txtLicensePlate, txtDate, txtStatus;
        txtName = (EditText) findViewById(R.id.editTextCust);
        txtLicensePlate = (EditText) findViewById(R.id.editTextLicensePlate);
        txtMobile = (EditText) findViewById(R.id.editTextMobile);
        txtDate = (EditText) findViewById(R.id.editTextDate);
        txtStatus = (EditText) findViewById(R.id.editTextStatus);

        txtName.setText(name);
        txtLicensePlate.setText(license_plate);
        txtMobile.setText(mobile);
        txtDate.setText(order_date);
        txtStatus.setText(common.ConvertCodeToStatus(status));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), UpdateStatusActivity.class);
                startActivity(i);
            }
        });
        btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateStatusOfOrder(common.ConvertStatusToCode("ดำเนินการเสร็จ"));
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateStatusOfOrder(common.ConvertStatusToCode("ยกเลิก"));
            }
        });
    }

    private void UpdateStatusOfOrder(final String status_code) {
        if ("-1".equals(status_code)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("คุณต้องการยกเลิกออเดอร์นี้?")
                    .setCancelable(false)
                    .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            DoUpdateStatus(status_code);
                        }
                    })
                    .setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            DoUpdateStatus(status_code);
        }
    }

    private void DoUpdateStatus(final String status_code) {
        String msgStatus = "";
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String url = getString(R.string.url) + "doUpdateStatus.php";
        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", order_id));
        params.add(new BasicNameValuePair("status", status_code));
        try {
            JSONArray data = new JSONArray(http.getJSONUrl(url, params));
            if (data.length() > 0) {
                JSONObject c = data.getJSONObject(0);
                msgStatus = c.getString("status");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            msgStatus = e.getMessage();
        }

        builder.setTitle("สถานะการบันทึก");
        final String finalMsgStatus = msgStatus;
        builder.setMessage(msgStatus).setCancelable(true).setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if ("ส่ง E-mail แจ้งลูกค้ารมาับรถเรียบร้อย".equals(finalMsgStatus) || "ยกเลิกการใช้บริการเรียบร้อย".equals(finalMsgStatus)) {
                           txtStatus.setText(common.ConvertCodeToStatus(status_code));
                        }
                        dialog.cancel();
                    }
                }

        ).show();
    }

}
