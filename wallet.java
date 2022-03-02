package com.example.smt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class wallet extends AppCompatActivity implements JsonResponse {
    EditText e1,e2;
    Button b;
    String amount;
    String[] amounts,types,dates,value;
    ListView l;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText)findViewById(R.id.et1);
        e2=(EditText)findViewById(R.id.ettotal);
        b=(Button)findViewById(R.id.bt1);
        l=(ListView)findViewById(R.id.l1);

        if(sh.getString("type","").equalsIgnoreCase("merchant"))
        {
            e1.setVisibility(View.GONE);
            b.setVisibility(View.GONE);
        }
        else{
            e1.setVisibility(View.VISIBLE);
            b.setVisibility(View.VISIBLE);
        }
        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse)wallet.this;
        String q="/viewwallet?lid="+sh.getString("log_id","");
        q=q.replace(" ","%20");
        JR.execute(q);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount=e1.getText().toString();
                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse)wallet.this;
                String q="/wallet?lid="+sh.getString("log_id","")+"&amount="+amount;
                q = q.replace(" ", "%20");
                JR.execute(q);
            }
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {

            String method=jo.getString("method");
            if(method.equalsIgnoreCase("wallet")) {

                String status = jo.getString("status");
                Log.d("pearl", status);


                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), "CREDITED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), wallet.class));

                } else {

                    Toast.makeText(getApplicationContext(), " failed.TRY AGAIN!!", Toast.LENGTH_LONG).show();
                }
            }
            else if(method.equalsIgnoreCase("viewwallet"))
            {
                String status=jo.getString("status");
                Log.d("pearl",status);


                if(status.equalsIgnoreCase("success")){
                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    e2.setText("Total Amount : "+jo.getString("tamount"));
                    amounts=new String[ja1.length()];
                    types=new String[ja1.length()];
                    dates=new String[ja1.length()];
                    value=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {
                        amounts[i]=ja1.getJSONObject(i).getString("amount");
                        types[i]=ja1.getJSONObject(i).getString("type");
                        dates[i]=ja1.getJSONObject(i).getString("date");
                        value[i]="Amount: "+amounts[i]+"\nType: "+types[i]+"\nDate: "+dates[i];

                    }
                    ArrayAdapter<String> ar=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,value);
                    l.setAdapter(ar);
                }
            }

        }

        catch (Exception e) {
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