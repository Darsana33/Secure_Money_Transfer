package com.example.smt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class payment extends AppCompatActivity implements JsonResponse{
    ListView l1;
    String [] amount,date,sname,rname,value;
    SharedPreferences sh;
    EditText e1;
    Integer mYear,mMonth,mDay;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1=(ListView)findViewById(R.id.l);
        e1=(EditText)findViewById(R.id.et) ;
        b1=(Button)findViewById(R.id.bt);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonReq JR=new JsonReq();
                JR.json_response=(JsonResponse)payment.this;
                String q="/viewpayments?lid="+sh.getString("log_id","")+"&search="+e1.getText().toString();
                q=q.replace(" ","%20");
                JR.execute(q);
            }
        });
        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                mYear  = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(payment.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "YYYY-MM-dd"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                        e1.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
        JsonReq JR=new JsonReq();
        JR.json_response=(JsonResponse)payment.this;
        String q="/viewpayment?lid="+sh.getString("log_id","");
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
                sname=new String[ja1.length()];
                rname=new String[ja1.length()];
                amount=new String[ja1.length()];
                date=new String[ja1.length()];
                value=new String[ja1.length()];

                for(int i = 0;i<ja1.length();i++)
                {
                    sname[i]=ja1.getJSONObject(i).getString("sname");
                    rname[i]=ja1.getJSONObject(i).getString("rname");
                    date[i]=ja1.getJSONObject(i).getString("date");
                    amount[i]=ja1.getJSONObject(i).getString("amount");
                    value[i]="sender: "+sname[i]+"\nreceiver: "+rname[i]+"\ndate: "+date[i]+"\namount: "+amount[i];

                }
                ArrayAdapter<String> ar=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,value);
                l1.setAdapter(ar);
            }

        }

        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

}
