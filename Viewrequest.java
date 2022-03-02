package com.example.smt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Viewrequest extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {
    ListView l;
    String noti;
    String[] not,value,date,user,amount,rid;
    public static String ridss,amountss,userss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewrequest);


        l=(ListView)findViewById(R.id.l1);
        l.setOnItemClickListener(this);
        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse)Viewrequest.this;
        String q="/viewmoneyrequest?lid="+login.logid;
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
                    rid=new String[ja1.length()];
                    user=new String[ja1.length()];
                    amount=new String[ja1.length()];
                    date=new String[ja1.length()];
                    value=new String[ja1.length()];

                    for(int i = 0;i<ja1.length();i++)
                    {
                        rid[i]=ja1.getJSONObject(i).getString("request_id");
                        user[i]=ja1.getJSONObject(i).getString("user");
                        amount[i]=ja1.getJSONObject(i).getString("amount");
                        date[i]=ja1.getJSONObject(i).getString("date");
                        value[i]="User: "+user[i]+"\nAmount: "+amount[i]+"\nDate: "+date[i];

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        userss = user[position];
        amountss = amount[position];
        ridss = rid[position];

        final CharSequence[] items = {"Make Payment", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Viewrequest.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Make Payment")) {
                    // startActivity(new Intent(getApplicationContext(), candidate_applyexam.class));

                    startActivity(new Intent(getApplicationContext(),UsermakenewPayment.class));


                }




                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();

    }
}