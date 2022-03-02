package com.example.smt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ip extends AppCompatActivity {
    EditText e;
    Button b;
    public static String ip;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e=(EditText)findViewById(R.id.et);
        e.setText(sh.getString("ip","192.168"));
        b=(Button)findViewById(R.id.bt);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip=e.getText().toString();
                if(ip.equals(""))
                {
                    e.setError("Enter ip address");
                    e.setFocusable(true);
                }
                else
                {
                    SharedPreferences.Editor e1=sh.edit();
                    e1.putString("ip",ip);
                    e1.commit();
                    startActivity(new Intent(getApplicationContext(),login.class));
                }
            }
        });
    }
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit  :")
                .setMessage("Are you sure you want to exit..?")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        // TODO Auto-generated method stub
                        Intent i=new Intent(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                }).setNegativeButton("No",null).show();

    }

}