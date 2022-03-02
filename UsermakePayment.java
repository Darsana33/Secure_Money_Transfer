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

public class UsermakePayment extends AppCompatActivity implements JsonResponse {

    EditText e1,e2;
    Button b1;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermake_payment);

        e1=(EditText)findViewById(R.id.etmerchant);
        e2=(EditText)findViewById(R.id.etamount);
        b1=(Button)findViewById(R.id.btpay);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        e1.setText("Merchant : "+AndroidBarcodeQrExample.merchant);
        e2.setText("Amount  : "+AndroidBarcodeQrExample.amount);

        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse)UsermakePayment.this;
        String q="/userviewbalance?lid="+sh.getString("log_id","")+"&amt="+AndroidBarcodeQrExample.amount;
        q=q.replace(" ","%20");
        JR.execute(q);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse)UsermakePayment.this;
                String q="/usermakepayment?lid="+sh.getString("log_id","")+"&mid="+AndroidBarcodeQrExample.mid+"&amount="+AndroidBarcodeQrExample.amount;
                q=q.replace(" ","%20");
                JR.execute(q);
            }
        });



    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            Log.d("pearl", method);


            if (method.equalsIgnoreCase("usermakepayment")) {

            String status = jo.getString("status");
            Log.d("pearl", status);


            if (status.equalsIgnoreCase("success")) {
                Toast.makeText(getApplicationContext(), "Payed Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), homeuser.class));

            } else {
                //startActivity(new Intent(getApplicationContext(), Image.class));

                Toast.makeText(getApplicationContext(), " failed.TRY AGAIN!!", Toast.LENGTH_LONG).show();
            }
        }
            if (method.equalsIgnoreCase("userviewbalance")) {
                String status = jo.getString("status");
                Log.d("pearl", status);

                if (!status.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), " You dont have enough money", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), wallet.class));
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}