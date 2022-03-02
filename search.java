package com.example.smt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class search extends AppCompatActivity implements JsonResponse {
    EditText e1,e2;
    Button b,b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        e1=(EditText)findViewById(R.id.et1);
        e2=(EditText)findViewById(R.id.et2);
        b=(Button)findViewById(R.id.bt1);
        b1=(Button)findViewById(R.id.bt2);
        if(login.usertype.equalsIgnoreCase("merchant"))
        {
            b1.setVisibility(View.GONE);
        }
        if(login.usertype.equalsIgnoreCase("user"))
        {
            b1.setVisibility(View.VISIBLE);
//            JsonReq JR=new JsonReq();
//            JR.json_response=(JsonResponse)search.this;
//            String q="/viewmoneyrequests?lid="+login.logid;
//            q = q.replace(" ", "%20");
//            JR.execute(q);
        }


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse)search.this;
                String q="/searchsnumber?num=" + e1.getText().toString()+"&amt="+e2.getText().toString()+"&lid="+login.logid;
                q = q.replace(" ", "%20");
                JR.execute(q);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Viewrequest.class));
            }
        });
    }


    public void response(JSONObject jo) {
        try {
            String status = jo.getString("status");
            Log.d("pearl", status);


            if (status.equalsIgnoreCase("success")) {
                Toast.makeText(getApplicationContext(), "Request sended ", Toast.LENGTH_LONG).show();
                if(login.usertype.equalsIgnoreCase("merchant"))
                {
                    startActivity(new Intent(getApplicationContext(),homemerchant.class));
                }
                if(login.usertype.equalsIgnoreCase("user"))
                {
                    startActivity(new Intent(getApplicationContext(),homeuser.class));
                }

            }
            else {
                //startActivity(new Intent(getApplicationContext(), Image.class));

                Toast.makeText(getApplicationContext(), " Number serached is not correct/ the number is not registerd yet", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        if(login.usertype.equalsIgnoreCase("merchant"))
        {
            startActivity(new Intent(getApplicationContext(),homemerchant.class));
        }
        if(login.usertype.equalsIgnoreCase("user"))
        {
            startActivity(new Intent(getApplicationContext(),homeuser.class));
        }


    }
}