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
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class userreg extends AppCompatActivity implements JsonResponse{
    EditText fname,lname,house,place,phone,email,uname,pass;
    String fname1,lname1,gender,house1,place1,phone1,email1,uname1,pass1;
    RadioButton female,male;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userreg);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        female = (RadioButton) findViewById(R.id.female);
        male = (RadioButton) findViewById(R.id.male);
        house = (EditText) findViewById(R.id.house);
        place = (EditText) findViewById(R.id.place);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        uname = (EditText) findViewById(R.id.uname);
        pass = (EditText) findViewById(R.id.pass);
        b = (Button) findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname1 = fname.getText().toString();
                lname1 = lname.getText().toString();
                house1 = house.getText().toString();
                place1 = place.getText().toString();
                phone1 = phone.getText().toString();
                email1 = email.getText().toString();
                uname1 = uname.getText().toString();
                pass1 = pass.getText().toString();
                if(female.isChecked())
                {
                    gender="female";
                }
                else
                {
                    gender="male";
                }
                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse)userreg.this;
                String q="/userreg?fname=" + fname1 +"&lname=" + lname1+"&gender="+gender+"&house=" +house1+ "&place=" + place1+"&phone="+phone1+"&email="+email1+"&uname="+uname1+"&pass="+pass1;
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