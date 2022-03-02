package com.example.smt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class merchantreg extends AppCompatActivity implements JsonResponse{
    EditText name,place,phone,email,uname,pass;
    String name1,place1,phone1,email1,uname1,pass1;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchantreg);
        name=(EditText)findViewById(R.id.name);
        place=(EditText)findViewById(R.id.place);
        phone=(EditText)findViewById(R.id.phone);
        email=(EditText)findViewById(R.id.email);
        uname=(EditText)findViewById(R.id.uname);
        pass=(EditText)findViewById(R.id.pass);
        b=(Button)findViewById(R.id.bt);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                name1=name.getText().toString();
                place1=place.getText().toString();
                phone1=phone.getText().toString();
                email1=email.getText().toString();
                uname1=uname.getText().toString();
                pass1=pass.getText().toString();
                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse)merchantreg.this;
                String q="/merchantreg?name=" + name1 + "&place=" + place1+"&phone="+phone1+"&email="+email1+"&uname="+uname1+"&pass="+pass1;
                q = q.replace(" ", "%20");
                JR.execute(q);

            }
        });


    }
    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("pearl", status);


            if (status.equalsIgnoreCase("success")) {
                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),login .class));

            }
            else {
                //startActivity(new Intent(getApplicationContext(), Image.class));

                Toast.makeText(getApplicationContext(), " failed.TRY AGAIN!!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}