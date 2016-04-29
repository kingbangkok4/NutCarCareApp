package com.nutcarcare.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

import com.database.DatabaseActivity;

/**
 * Created by Administrator on 1/20/2016.
 */
public class IntroActivity extends Activity {
   // private DatabaseActivity myDb = new DatabaseActivity(this);
    ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //myDb.openDatabase();
        setContentView(R.layout.activity_intro);
        LinearLayout llProgress = (LinearLayout) findViewById(R.id.ll_progress);
        try {
            // give your gif image name here(example.gif).
            GIFView gif = new GIFView(this, "file:///android_asset/loading.gif");
            llProgress.addView(gif);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //MyArrList = myDb.CheckLogin();
              /*  if (MyArrList != null) {
                    if(MyArrList.size() > 0){
                        Intent i = new Intent(IntroActivity.this, ServiceActivity.class);
                        i.putExtra("MyArrList", MyArrList);
                        startActivity(i);
                    }else {*/
                        Intent i = new Intent(IntroActivity.this, MainActivity.class);
                        startActivity(i);
                   /* }
                } else {
                    Intent i = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(i);
                }*/
                finish();
            }
        }, 5000);
    }
}

