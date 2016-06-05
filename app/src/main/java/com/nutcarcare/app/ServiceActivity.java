package com.nutcarcare.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.http.Http;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 1/20/2016.
 * Code formatting shortcut in Android studio : Ctrl + Atl + L
 */
public class ServiceActivity extends Activity {
  /*  private CheckBox ck1, ck2, ck3, ck4, ck5, ck6, ck7, ck8, ck9, ck10, ck11, ck12, ck13, ck14, ck15, ck16, ck17;*/
    private EditText txtTotal;
    private Button btTotal, btSubmit, btMain;
    private Double sumTotal = 0.00;
    private StringBuilder strDetailService = new StringBuilder();
    private DecimalFormat decimalFormat = new DecimalFormat("#,###,###.##");
    //private DatabaseActivity myDb = new DatabaseActivity(this);
    private ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> tmpMyArrList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> ArrListService = new ArrayList<HashMap<String, String>>();
    ListView listVservice;
    private Http http = new Http();
    HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

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

        listVservice = (ListView)findViewById(R.id.listViewService);
        txtTotal = (EditText) findViewById(R.id.editTextTotal);
        btTotal = (Button) findViewById(R.id.btnTotal);
        btSubmit = (Button) findViewById(R.id.btnSubmit);
        btMain = (Button) findViewById(R.id.btnMain);

        btTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SumTotal();
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToSubmit();
            }
        });
        btMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MenuActivity.class);
                i.putExtra("MyArrList", MyArrList);
                startActivity(i);
            }
        });

        GetService();
    }

    private void GetService() {
        String url = getString(R.string.url) + "serviceListJson.php";
        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", ""));
        try {
            JSONArray data = new JSONArray(http.getJSONUrl(url, params));
            if (data.length() > 0) {
                ArrListService.clear();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    map = new HashMap<String, String>();
                    map.put("id", c.getString("id"));
                    map.put("name", c.getString("name"));
                    map.put("price", c.getString("price"));
                    map.put("created_date", c.getString("created_date"));
                    map.put("promotion", c.getString("promotion"));
                    map.put("active", c.getString("active"));
                    map.put("currency", "บาท");
                    ArrListService.add(map);
                }

                SimpleAdapter sAdap;
                sAdap = new SimpleAdapter(getBaseContext(), ArrListService,
                        R.layout.activity_column_service, new String[]{"name", "currency", "price", ""}, new int[]{R.id.ColName, R.id.ColCurrency, R.id.ColPrice, R.id.ColChk});
                listVservice.setAdapter(sAdap);
               /* listVservice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView,
                                            int position, long mylng) {

                    }
                });*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void SumTotal() {
        sumTotal = 0.00;
        strDetailService.setLength(0);
        txtTotal.setText("");

        int count = listVservice.getAdapter().getCount();
        for (int i = 0; i < count; i++) {
            LinearLayout itemLayout = (LinearLayout)listVservice.getChildAt(i); // Find by under LinearLayout
            CheckBox checkbox = (CheckBox)itemLayout.findViewById(R.id.ColChk);
            if(checkbox.isChecked())
            {
                sumTotal += Double.parseDouble(ArrListService.get(i).get("price").toString());
                strDetailService.append(ArrListService.get(i).get("name").toString()
                        +"("
                        +ArrListService.get(i).get("price").toString()
                        +")"
                        +" "
                );

               /* Log.d("name " + String.valueOf(i), checkbox.getTag().toString());
                Toast.makeText(getBaseContext(), checkbox.getTag().toString(), Toast.LENGTH_LONG).show();*/
            }
        }

     /*   if (ck1.isChecked()) {
            sumTotal += 150.00;
            ทำความสะอาดภายใน (150 บาท) ");
        }
        if (ck2.isChecked()) {
            sumTotal += 300.00;
            strDetailService.append("อบโอโซน (300 บาท) ");
        }
        if (ck3.isChecked()) {
            sumTotal += 500.00;
            strDetailService.append("ขัดกระจกลบรอยน้ำ (500 บาท) ");
        }
        if (ck4.isChecked()) {
            sumTotal += 300.00;
            strDetailService.append("ขัดล้อแม็กซ์ (300 บาท) ");
        }
        if (ck5.isChecked()) {
            sumTotal += 300.00;
            strDetailService.append("เคลือบสีด้วยน้ำยา (300 บาท) ");
        }
        if (ck6.isChecked()) {
            sumTotal += 450.00;
            strDetailService.append("เคลือบกระจก (450  บาท) ");
        }
        if (ck7.isChecked()) {
            sumTotal += 300.00;
            strDetailService.append("เคลือบล้อแม็กซ์ (300 บาท) ");
        }
        if (ck8.isChecked()) {
            sumTotal += 500.00;
            strDetailService.append("ขจัดคราบยางมะตอย (500 บาท) ");
        }

        if (ck9.isChecked()) {
            sumTotal += 150.00;
            strDetailService.append("ล้างสีภายนอก (150 บาท) ");
        }
        if (ck10.isChecked()) {
            sumTotal += 250.00;
            strDetailService.append("ล้างสี + ดูดฝุ่น (250 บาท) ");
        }
        if (ck11.isChecked()) {
            sumTotal += 200.00;
            strDetailService.append("ล้างห้องเครื่อง (200 บาท) ");
        }
        if (ck12.isChecked()) {
            sumTotal += 200.00;
            strDetailService.append("ล้างอัดฉีดช่วงล่าง (200 บาท) ");
        }
        if (ck13.isChecked()) {
            sumTotal += 450.00;
            strDetailService.append("ขัดเคลือบสี + ลงแว๊กซ์ (450 บาท) ");
        }
        if (ck14.isChecked()) {
            sumTotal += 300.00;
            strDetailService.append("ขัดเคลือบไฟหน้า (300 บาท) ");
        }
        if (ck15.isChecked()) {
            sumTotal += 280.00;
            strDetailService.append("ขัดเบาะหนัง (280 บาท) ");
        }
        if (ck16.isChecked()) {
            sumTotal += 300.00;
            strDetailService.append("ฟอกพรม (300 บาท) ");
        }
        if (ck17.isChecked()) {
            sumTotal += 320.00;
            strDetailService.append("ฟอกเบาะพรม (320 บาท) ");
        }*/

        txtTotal.setText(decimalFormat.format(sumTotal).toString()+" บาท");
        txtTotal.setTextColor(Color.BLUE);
    }

    private void GoToSubmit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        SumTotal();
        String strService = strDetailService.toString();
        if(!"".equals(strService)){
            Intent i = new Intent(getBaseContext(), CustomerActivity.class);
            i.putExtra("MyArrList", MyArrList);
            i.putExtra("sumTotal", sumTotal);
            i.putExtra("strService", strService);
            startActivity(i);
        }else {
            builder.setTitle("แจ้งเตือน");
            builder.setMessage("กรุณาเลือกบริการอย่างน้อย 1 รายการ !")
                    .setCancelable(true)
                    .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    *//**
     * On selecting action bar icons
     * *//*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_logout:
                // logout action
                //myDb.DeleleLogin();
                Intent i = new Intent(ServiceActivity.this, MainActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


}
