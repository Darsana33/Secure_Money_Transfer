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

public class complaint extends AppCompatActivity implements JsonResponse{
    EditText e1;
    Button b;
    String complaint;
    ListView l;
    String[] compl,reply,date,value;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText)findViewById(R.id.et1);
        b=(Button)findViewById(R.id.bt1);
        l=(ListView)findViewById(R.id.l1);
        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse)complaint.this;
        String q="/viewcomplaints?lid="+sh.getString("log_id","");
        q=q.replace(" ","%20");
        JR.execute(q);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaint=e1.getText().toString();
                JsonReq JR = new JsonReq();
                JR.json_response = (JsonResponse) complaint.this;
                String q = "/sendcomplaints?complaint=" + complaint + "&lid=" + sh.getString("log_id", "")+"&type="+login.usertype;
                q = q.replace(" ", "%20");
                JR.execute(q);
            }
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {

            String method=jo.getString("method");
            if(method.equalsIgnoreCase("sendcomplaints")) {

                String status = jo.getString("status");
                Log.d("pearl", status);


                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), "SEND SUCCESSFULLY", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), complaint.class));

                } else {

                    Toast.makeText(getApplicationContext(), " failed.TRY AGAIN!!", Toast.LENGTH_LONG).show();
                }
            }
            else if(method.equalsIgnoreCase("viewcomplaints"))
            {
                String status=jo.getString("status");
                Log.d("pearl",status);


                if(status.equalsIgnoreCase("success")){
                    JSONArray ja1=(JSONArray)jo.getJSONArray("data");
                    //feedback_id=new String[ja1.length()];
                    compl=new String[ja1.length()];
                    reply=new String[ja1.length()];
                    date=new String[ja1.length()];
                    value=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {
                        compl[i]=ja1.getJSONObject(i).getString("complaint");
                        reply[i]=ja1.getJSONObject(i).getString("reply");
                        date[i]=ja1.getJSONObject(i).getString("date");
                        value[i]="complaints: "+compl[i]+"\nreply: "+reply[i]+"\ndate: "+date[i];

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