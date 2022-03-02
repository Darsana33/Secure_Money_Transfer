package com.example.smt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class notification extends AppCompatActivity implements JsonResponse{
    ListView l;
    String noti;
    String[] not,value,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        l=(ListView)findViewById(R.id.l1);
        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse)notification.this;
        String q="/viewnotification";
        q=q.replace(" ","%20");
        JR.execute(q);
    }
    @Override
    public void response(JSONObject jo) {
        try {

                String status=jo.getString("status");
                Log.d("pearl",status);


                if(status.equalsIgnoreCase("success")){
                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    //feedback_id=new String[ja1.length()];
                    not=new String[ja1.length()];
                    date=new String[ja1.length()];
                    value=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {
                        not[i]=ja1.getJSONObject(i).getString("notification");
                        date[i]=ja1.getJSONObject(i).getString("date");
                        value[i]="notification: "+not[i]+"\ndate: "+date[i];

                    }
                    ArrayAdapter<String> ar=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,value);
                    l.setAdapter(ar);
                }

        }

        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

}