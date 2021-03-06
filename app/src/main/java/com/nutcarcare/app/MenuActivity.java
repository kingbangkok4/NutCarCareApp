package com.nutcarcare.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 1/20/2016.
 * Code formatting shortcut in Android studio : Ctrl + Atl + L
 */
public class MenuActivity extends Activity {
    private Button btService, btReceive, btnExit, btnUpStatus;
    private Double sumTotal = 0.00;
    private StringBuilder strDetailService = new StringBuilder();
    private DecimalFormat decimalFormat = new DecimalFormat("#,###,###.##");
    //private DatabaseActivity myDb = new DatabaseActivity(this);
    ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> tmpMyArrList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Bundle extras = getIntent().getExtras();
        // เช็คว่ามีค่าที่ส่งมาจากหน้าอื่นหรือไม่ถ้ามีจะไม่เท่ากับ null
        if (extras != null) {
            tmpMyArrList = (ArrayList<HashMap<String, String>>) extras
                    .getSerializable("MyArrList");
            if (tmpMyArrList != null) {
                MyArrList = tmpMyArrList;
            }
        }

        CheckLogin();

        btService = (Button) findViewById(R.id.btnService);
        btReceive = (Button) findViewById(R.id.btnReceive);
        btnExit =  (Button) findViewById(R.id.btnExit);
        btnUpStatus = (Button) findViewById(R.id.btnUpStatus);

        btService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), ServiceActivity.class);
                i.putExtra("MyArrList", MyArrList);
                startActivity(i);
            }
        });
        btnUpStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), UpdateStatusActivity.class);
                i.putExtra("MyArrList", MyArrList);
                startActivity(i);
            }
        });
        btReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), ReceiveActivity.class);
                i.putExtra("MyArrList", MyArrList);
                startActivity(i);
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void CheckLogin() {
        if(MyArrList == null){
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
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
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


}
