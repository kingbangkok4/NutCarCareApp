package com.nutcarcare.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 1/20/2016.
 * Code formatting shortcut in Android studio : Ctrl + Atl + L
 */
public class ServiceActivity extends Activity {
    private CheckBox ck1, ck2, ck3, ck4, ck5, ck6, ck7, ck8, ck9, ck10, ck11;
    private EditText txtTotal;
    private Button btTotal, btSubmit;
    private Double sumTotal = 0.00;
    private StringBuilder strDetailService = new StringBuilder();
    private DecimalFormat decimalFormat = new DecimalFormat("#,###,###.##");
    ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tmpMyArrList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Bundle extras = getIntent().getExtras();
        // เช็คว่ามีค่าที่ส่งมาจากหน้าอื่นหรือไม่ถ้ามีจะไม่เท่ากับ null
        if (extras != null) {
            tmpMyArrList = (ArrayList<HashMap<String, String>>) extras
                    .getSerializable("MyArrList");
            if (tmpMyArrList != null) {
                MyArrList = tmpMyArrList;
            }
        }

        ck1 = (CheckBox) findViewById(R.id.checkBox1);
        ck2 = (CheckBox) findViewById(R.id.checkBox2);
        ck3 = (CheckBox) findViewById(R.id.checkBox3);
        ck4 = (CheckBox) findViewById(R.id.checkBox4);
        ck5 = (CheckBox) findViewById(R.id.checkBox5);
        ck6 = (CheckBox) findViewById(R.id.checkBox6);
        ck7 = (CheckBox) findViewById(R.id.checkBox7);
        ck8 = (CheckBox) findViewById(R.id.checkBox8);
        ck9 = (CheckBox) findViewById(R.id.checkBox9);
        ck10 = (CheckBox) findViewById(R.id.checkBox10);
        ck11 = (CheckBox) findViewById(R.id.checkBox11);

        txtTotal = (EditText) findViewById(R.id.editTextTotal);
        btTotal = (Button) findViewById(R.id.btnTotal);
        btSubmit = (Button) findViewById(R.id.btnSubmit);

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
    }

    private void SumTotal() {
        sumTotal = 0.00;
        strDetailService.setLength(0);
        txtTotal.setText("");
        if (ck1.isChecked()) {
            sumTotal += 120.00;
            strDetailService.append("ล้างสีภายนอก (120 บาท) ");
        }
        if (ck2.isChecked()) {
            sumTotal += 200.00;
            strDetailService.append("ล้างสี+ดูดฝุ่น (200 บาท) ");
        }
        if (ck3.isChecked()) {
            sumTotal += 250.00;
            strDetailService.append("ล้างห้องเครื่อง (250 บาท) ");
        }
        if (ck4.isChecked()) {
            sumTotal += 120.00;
            strDetailService.append("ล้างอัดฉีดช่วงล่าง (120 บาท) ");
        }
        if (ck5.isChecked()) {
            sumTotal += 300.00;
            strDetailService.append("ขัดเคลือบสี (300 บาท) ");
        }
        if (ck6.isChecked()) {
            sumTotal += 250.00;
            strDetailService.append("ลงแว๊กซี่ (250  บาท) ");
        }
        if (ck7.isChecked()) {
            sumTotal += 400.00;
            strDetailService.append("ขัดลบรอยขีดข่วน (400 บาท) ");
        }
        if (ck8.isChecked()) {
            sumTotal += 200.00;
            strDetailService.append("ขัดเคลือบไฟหน้า (200 บาท) ");
        }
        if (ck9.isChecked()) {
            sumTotal += 200.00;
            strDetailService.append("ขัดเบาะหนัง (200 บาท) ");
        }
        if (ck10.isChecked()) {
            sumTotal += 200.00;
            strDetailService.append("ฟอกพรม(200 บาท) ");
        }
        if (ck11.isChecked()) {
            sumTotal += 500.00;
            strDetailService.append("ฟอกเบาะพรม (500 บาท) ");
        }
        txtTotal.setText(decimalFormat.format(sumTotal).toString()+" บาท");
        txtTotal.setTextColor(Color.BLUE);
    }

    private void GoToSubmit() {
        String strService = strDetailService.toString();
        Intent i = new Intent(ServiceActivity.this, CustomerActivity.class);
        i.putExtra("MyArrList", MyArrList);
        i.putExtra("sumTotal", sumTotal);
        i.putExtra("strService", strService);
        startActivity(i);
    }

}
