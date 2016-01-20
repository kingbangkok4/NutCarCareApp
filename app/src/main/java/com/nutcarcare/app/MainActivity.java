package com.nutcarcare.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.http.Http;

public class MainActivity extends Activity  {
    Button btLogin;
    EditText txtUsername, txtPassword;
    Http http = new Http();

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

        btLogin=(Button)findViewById(R.id.btnLogin);
        txtUsername=(EditText)findViewById(R.id.editTextUsername);
        txtPassword=(EditText)findViewById(R.id.editTextPassword);


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtUsername.getText().toString().equals("admin") &&
                        txtPassword.getText().toString().equals("1234")) {
                    Toast.makeText(getApplicationContext(), "Redirecting...",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private  void  OnLogin(){
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        String url = getString(R.string.url_localhost);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status", "login"));
        params.add(new BasicNameValuePair("strUser", txtUsername.getText()
                .toString().trim()));
        params.add(new BasicNameValuePair("strPass", txtPassword.getText()
                .toString().trim()));

        String resultServer = http.getHttpPost(url, params);

        /*** Default Value ***/
        String strStatusID = "0";
        String strType = "0";
        String strError = "Unknow Status!";

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strStatusID = c.getString("StatusID");
            strType = c.getString("MemberID");
            strError = c.getString("Error");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Prepare Login
        if (strStatusID.equals("0")) {
            // Dialog
            ad.setTitle("Error! ");
            ad.setIcon(android.R.drawable.btn_star_big_on);
            ad.setPositiveButton("Close", null);
            ad.setMessage(strError);
            ad.show();
            txtUsername.setText("");
            txtUsername.setText("");
        } else {
            Toast.makeText(MainActivity.this, "Login OK",
                    Toast.LENGTH_SHORT).show();
            Intent newActivity = new Intent(MainActivity.this,
                    ServiceActivity.class);
            newActivity.putExtra("Type", strType);
            startActivity(newActivity);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
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
    }
}