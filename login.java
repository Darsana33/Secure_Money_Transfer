package com.example.smt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class login extends AppCompatActivity implements JsonResponse{
    EditText e1,e2;
    Button b;
    TextView t;
    public static String username,password,logid,usertype,merchantname,merchantid;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText)findViewById(R.id.et1);
        e2=(EditText)findViewById(R.id.et2);
        b=(Button)findViewById(R.id.bt1);
        t=(TextView)findViewById(R.id.bt2);

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),regmenu.class));


            }
        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=e1.getText().toString();
                password=e2.getText().toString();
                if(username.equalsIgnoreCase(""))
                {
                    e1.setError("Enter your Username");
                    e1.setFocusable(true);
                }
                else if(password.equalsIgnoreCase(""))
                {
                    e2.setError("Enter your Password");
                    e2.setFocusable(true);
                }
                else{
                    JsonReq JR=new JsonReq();
                    JR.json_response=(JsonResponse)login.this;
                    String q="/login?username=" + username + "&password=" + password;
                    q = q.replace(" ", "%20");
                    JR.execute(q);

                }
            }
        });
    }
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (status.equalsIgnoreCase("success")) {
                JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                logid = ja1.getJSONObject(0).getString("login_id");
                usertype = ja1.getJSONObject(0).getString("usertype");
                merchantname = jo.getString("uname");
                merchantid = jo.getString("uid");

                SharedPreferences.Editor e = sh.edit();
                e.putString("log_id", logid);
                e.commit();

                if (usertype.equals("merchant")) {
                    Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),homemerchant.class));
                }

                else if(usertype.equals("user"))
                {
                    Toast.makeText(getApplicationContext(),"Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),homeuser.class));
                }

            }
            else {
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        } catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
       startActivity(new Intent(getApplicationContext(),ip.class));

    }



}