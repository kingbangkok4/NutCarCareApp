package com.nutcarcare.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.database.DatabaseActivity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 1/20/2016.
 * Code formatting shortcut in Android studio : Ctrl + Atl + L
 */
public class ServiceActivity extends Activity {
    private CheckBox ck1, ck2, ck3, ck4, ck5, ck6, ck7, ck8;
    private EditText txtTotal;
    private Button btTotal, btSubmit, btMain;
    private Double sumTotal = 0.00;
    private StringBuilder strDetailService = new StringBuilder();
    private DecimalFormat decimalFormat = new DecimalFormat("#,###,###.##");
    //private DatabaseActivity myDb = new DatabaseActivity(this);
    private ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> tmpMyArrList = new ArrayList<HashMap<String, String>>();

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
    }

    private void SumTotal() {
        sumTotal = 0.00;
        strDetailService.setLength(0);
        txtTotal.setText("");
        if (ck1.isChecked()) {
            sumTotal += 150.00;
            strDetailService.append("ทำความสะอาดภายใน (150 บาท) ");
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
