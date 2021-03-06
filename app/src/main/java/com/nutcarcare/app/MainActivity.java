package com.nutcarcare.app;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.http.Http;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    //private DatabaseActivity myDb = new DatabaseActivity(this);
    ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map;
    private Button btLogin;
    private EditText txtUsername, txtPassword;
    private Http http = new Http();
    /*    private String strUsername = "";
        private String strPassword = "";
        private String strType = "";*/
    private String strError = "Unknow Status!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btLogin = (Button) findViewById(R.id.btnService);
        txtUsername = (EditText) findViewById(R.id.editTextUsername);
        txtPassword = (EditText) findViewById(R.id.editTextPassword);


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  // TestศToast.makeText(getApplicationContext(), strError, Toast.LENGTH_SHORT).show();
                map = new HashMap<String, String>();
                map.put("username", "staff");
                map.put("password", "1234");
                map.put("type", "Staff");
                MyArrList.add(map);
                Intent ii = new Intent(MainActivity.this, ServiceActivity.class);
                ii.putExtra("MyArrList", MyArrList);
                startActivity(ii);*/
                //************ end test
                Boolean status = OnLogin();
                if (status) {
                    Toast.makeText(getApplicationContext(), strError, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getBaseContext(), MenuActivity.class);
                    i.putExtra("MyArrList", MyArrList);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), strError, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean OnLogin() {
/*        final AlertDialog.Builder ad = new AlertDialog.Builder(this);*/
        Boolean ststusLogin = false;
        String Error = "1";
        String url = getString(R.string.url) + "checkLoginJson.php";
        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", txtUsername.getText()
                .toString().trim()));
        params.add(new BasicNameValuePair("password", txtPassword.getText()
                .toString().trim()));
        try {
            JSONArray data = new JSONArray(http.getJSONUrl(url, params));
            if (data.length() > 0) {
                JSONObject c = data.getJSONObject(0);
                Error = c.getString("error");
                if ("0".equals(Error)) {
                    map = new HashMap<String, String>();
                    map.put("username", c.getString("username"));
                    map.put("password", c.getString("password"));
                    map.put("type", c.getString("type"));
                    MyArrList.add(map);

                    //myDb.InsertLogin(MyArrList.get(0).get("username"), MyArrList.get(0).get("password"), MyArrList.get(0).get("type"));
                }
            }

            if ("0".equals(Error)) {
                ststusLogin = true;
                strError = "รหัสผ่านถูกต้อง...";
            } else {
                ststusLogin = false;
                strError = "รหัสผ่านไม่ถูกต้อง";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ststusLogin = false;
            strError = e.getMessage();
        }
        return ststusLogin;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}